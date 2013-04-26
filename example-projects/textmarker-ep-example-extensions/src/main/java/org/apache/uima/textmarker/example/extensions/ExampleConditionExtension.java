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

package org.apache.uima.textmarker.example.extensions;

import java.util.List;

import org.apache.uima.textmarker.TextMarkerElement;
import org.apache.uima.textmarker.condition.AbstractTextMarkerCondition;
import org.apache.uima.textmarker.expression.TextMarkerExpression;
import org.apache.uima.textmarker.expression.string.StringExpression;
import org.apache.uima.textmarker.extensions.ITextMarkerConditionExtension;
import org.apache.uima.textmarker.verbalize.TextMarkerVerbalizer;

import antlr.ANTLRException;

public class ExampleConditionExtension implements ITextMarkerConditionExtension {

  private final String[] knownExtensions = new String[] { "ExampleCondition" };

  private final Class<?>[] extensions = new Class[] { ExampleCondition.class };

  public String verbalize(TextMarkerElement element, TextMarkerVerbalizer verbalizer) {
    if (element instanceof ExampleCondition) {
      ExampleCondition c = (ExampleCondition) element;
      return verbalizeName(element) + "(" + verbalizer.verbalize(c.getExpr()) + ", "
              + verbalizer.verbalize(c.getFormatExpr()) + ")";
    } else {
      return "UnknownAction";
    }
  }

  public AbstractTextMarkerCondition createCondition(String name, List<TextMarkerExpression> args)
          throws ANTLRException {
    if (args != null && args.size() == 2) {
      if (!(args.get(0) instanceof StringExpression)) {
      }
      if (!(args.get(1) instanceof StringExpression)) {
      }
    } else {
      throw new ANTLRException(
              "ExampleCondition accepts exactly two StringExpressions as arguments");
    }
    return new ExampleCondition((StringExpression) args.get(0), (StringExpression) args.get(1));
  }

  public String verbalizeName(TextMarkerElement element) {
    return knownExtensions[0];
  }

  public String[] getKnownExtensions() {
    return knownExtensions;
  }

  public Class<?>[] extensions() {
    return extensions;
  }

}
