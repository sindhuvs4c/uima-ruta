/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.uima.ruta.rule.quantifier;

import java.util.List;

import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.ruta.RutaStream;
import org.apache.uima.ruta.expression.number.INumberExpression;
import org.apache.uima.ruta.expression.number.SimpleNumberExpression;
import org.apache.uima.ruta.rule.ComposedRuleElementMatch;
import org.apache.uima.ruta.rule.MatchContext;
import org.apache.uima.ruta.rule.RuleElementMatch;
import org.apache.uima.ruta.visitor.InferenceCrowd;

public class MinMaxGreedy extends AbstractRuleElementQuantifier {

  private INumberExpression min;

  private INumberExpression max;

  public MinMaxGreedy(INumberExpression min, INumberExpression max, boolean interval) {
    super();
    if (!interval) {
      this.min = min;
      this.max = min;
    } else {
      this.min = min;
      this.max = max;
      if (this.max == null) {
        this.max = new SimpleNumberExpression(Integer.MAX_VALUE);
      }
    }
  }

  @Override
  public List<RuleElementMatch> evaluateMatches(List<RuleElementMatch> matches,
          MatchContext context, RutaStream stream, InferenceCrowd crowd) {
    int minValue = min.getIntegerValue(context, stream);
    int maxValue = max.getIntegerValue(context, stream);

    if (matches.size() > 0) {
      RuleElementMatch ruleElementMatch = matches.get(matches.size() - 1);
      if (!ruleElementMatch.matched()) {
        matches.remove(ruleElementMatch);
      }
    }

    int matchedSize = matches.size();

    boolean result = matchedSize >= minValue && matchedSize <= maxValue;
    if (result) {
      return matches;
    } else {
      return null;
    }
  }

  public INumberExpression getMin() {
    return min;
  }

  public INumberExpression getMax() {
    return max;
  }

  @Override
  public boolean continueMatch(boolean after, MatchContext context, AnnotationFS annotation,
          ComposedRuleElementMatch containerMatch, RutaStream stream, InferenceCrowd crowd) {
    if (annotation == null) {
      // do not try to continue a match that totally failed
      return false;
    }
    int minValue = min.getIntegerValue(context, stream);
    int maxValue = max.getIntegerValue(context, stream);
    List<RuleElementMatch> list = containerMatch.getInnerMatches().get(context.getElement());
    if (list == null) {
      if (maxValue > 0) {
        return true;
      } else {
        return false;
      }
    }
    int matchedSize = list.size();
    if (list == null || list.isEmpty() || matchedSize < minValue) {
      return true;
    }
    RuleElementMatch lastMatch = null;
    if (after) {
      lastMatch = list.get(list.size() - 1);
    } else {
      lastMatch = list.get(0);
    }
    return matchedSize < maxValue
            || (!lastMatch.matched() && matchedSize >= minValue && matchedSize <= maxValue);
  }

  @Override
  public boolean isOptional(MatchContext context, RutaStream stream) {
    int minValue = min.getIntegerValue(context, stream);
    return minValue == 0;
  }
}
