// Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
// or more contributor license agreements. Licensed under the Elastic License
// 2.0; you may not use this file except in compliance with the Elastic License
// 2.0.
package org.elasticsearch.xpack.esql.expression.function.scalar.multivalue;

import java.lang.ArithmeticException;
import java.lang.Override;
import java.lang.String;
import org.elasticsearch.compute.data.Block;
import org.elasticsearch.compute.data.IntBlock;
import org.elasticsearch.compute.operator.DriverContext;
import org.elasticsearch.compute.operator.EvalOperator;
import org.elasticsearch.xpack.esql.expression.function.Warnings;
import org.elasticsearch.xpack.ql.tree.Source;

/**
 * {@link EvalOperator.ExpressionEvaluator} implementation for {@link MvSum}.
 * This class is generated. Do not edit it.
 */
public final class MvSumIntEvaluator extends AbstractMultivalueFunction.AbstractNullableEvaluator {
  private final Warnings warnings;

  private final DriverContext driverContext;

  public MvSumIntEvaluator(Source source, EvalOperator.ExpressionEvaluator field,
      DriverContext driverContext) {
    super(field);
    this.warnings = new Warnings(source);
    this.driverContext = driverContext;
  }

  @Override
  public String name() {
    return "MvSum";
  }

  /**
   * Evaluate blocks containing at least one multivalued field.
   */
  @Override
  public Block.Ref evalNullable(Block.Ref ref) {
    try (ref) {
      IntBlock v = (IntBlock) ref.block();
      int positionCount = v.getPositionCount();
      IntBlock.Builder builder = IntBlock.newBlockBuilder(positionCount, driverContext.blockFactory());
      for (int p = 0; p < positionCount; p++) {
        int valueCount = v.getValueCount(p);
        if (valueCount == 0) {
          builder.appendNull();
          continue;
        }
        try {
          int first = v.getFirstValueIndex(p);
          int end = first + valueCount;
          int value = v.getInt(first);
          for (int i = first + 1; i < end; i++) {
            int next = v.getInt(i);
            value = MvSum.process(value, next);
          }
          int result = value;
          builder.appendInt(result);
        } catch (ArithmeticException e) {
          warnings.registerException(e);
          builder.appendNull();
        }
      }
      return Block.Ref.floating(builder.build());
    }
  }
}
