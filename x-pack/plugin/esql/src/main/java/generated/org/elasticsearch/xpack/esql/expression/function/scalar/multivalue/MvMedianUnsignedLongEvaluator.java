// Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
// or more contributor license agreements. Licensed under the Elastic License
// 2.0; you may not use this file except in compliance with the Elastic License
// 2.0.
package org.elasticsearch.xpack.esql.expression.function.scalar.multivalue;

import java.lang.Override;
import java.lang.String;
import org.elasticsearch.compute.data.Block;
import org.elasticsearch.compute.data.LongBlock;
import org.elasticsearch.compute.data.LongVector;
import org.elasticsearch.compute.operator.DriverContext;
import org.elasticsearch.compute.operator.EvalOperator;

/**
 * {@link EvalOperator.ExpressionEvaluator} implementation for {@link MvMedian}.
 * This class is generated. Do not edit it.
 */
public final class MvMedianUnsignedLongEvaluator extends AbstractMultivalueFunction.AbstractEvaluator {
  private final DriverContext driverContext;

  public MvMedianUnsignedLongEvaluator(EvalOperator.ExpressionEvaluator field,
      DriverContext driverContext) {
    super(field);
    this.driverContext = driverContext;
  }

  @Override
  public String name() {
    return "MvMedian";
  }

  /**
   * Evaluate blocks containing at least one multivalued field.
   */
  @Override
  public Block.Ref evalNullable(Block.Ref ref) {
    if (ref.block().mvSortedAscending()) {
      return evalAscendingNullable(ref);
    }
    try (ref) {
      LongBlock v = (LongBlock) ref.block();
      int positionCount = v.getPositionCount();
      LongBlock.Builder builder = LongBlock.newBlockBuilder(positionCount, driverContext.blockFactory());
      MvMedian.Longs work = new MvMedian.Longs();
      for (int p = 0; p < positionCount; p++) {
        int valueCount = v.getValueCount(p);
        if (valueCount == 0) {
          builder.appendNull();
          continue;
        }
        int first = v.getFirstValueIndex(p);
        int end = first + valueCount;
        for (int i = first; i < end; i++) {
          long value = v.getLong(i);
          MvMedian.processUnsignedLong(work, value);
        }
        long result = MvMedian.finishUnsignedLong(work);
        builder.appendLong(result);
      }
      return Block.Ref.floating(builder.build());
    }
  }

  /**
   * Evaluate blocks containing at least one multivalued field.
   */
  @Override
  public Block.Ref evalNotNullable(Block.Ref ref) {
    if (ref.block().mvSortedAscending()) {
      return evalAscendingNotNullable(ref);
    }
    try (ref) {
      LongBlock v = (LongBlock) ref.block();
      int positionCount = v.getPositionCount();
      LongVector.FixedBuilder builder = LongVector.newVectorFixedBuilder(positionCount, driverContext.blockFactory());
      MvMedian.Longs work = new MvMedian.Longs();
      for (int p = 0; p < positionCount; p++) {
        int valueCount = v.getValueCount(p);
        int first = v.getFirstValueIndex(p);
        int end = first + valueCount;
        for (int i = first; i < end; i++) {
          long value = v.getLong(i);
          MvMedian.processUnsignedLong(work, value);
        }
        long result = MvMedian.finishUnsignedLong(work);
        builder.appendLong(result);
      }
      return Block.Ref.floating(builder.build().asBlock());
    }
  }

  /**
   * Evaluate blocks containing at least one multivalued field and all multivalued fields are in ascending order.
   */
  private Block.Ref evalAscendingNullable(Block.Ref ref) {
    try (ref) {
      LongBlock v = (LongBlock) ref.block();
      int positionCount = v.getPositionCount();
      LongBlock.Builder builder = LongBlock.newBlockBuilder(positionCount, driverContext.blockFactory());
      MvMedian.Longs work = new MvMedian.Longs();
      for (int p = 0; p < positionCount; p++) {
        int valueCount = v.getValueCount(p);
        if (valueCount == 0) {
          builder.appendNull();
          continue;
        }
        int first = v.getFirstValueIndex(p);
        long result = MvMedian.ascendingUnsignedLong(v, first, valueCount);
        builder.appendLong(result);
      }
      return Block.Ref.floating(builder.build());
    }
  }

  /**
   * Evaluate blocks containing at least one multivalued field and all multivalued fields are in ascending order.
   */
  private Block.Ref evalAscendingNotNullable(Block.Ref ref) {
    try (ref) {
      LongBlock v = (LongBlock) ref.block();
      int positionCount = v.getPositionCount();
      LongVector.FixedBuilder builder = LongVector.newVectorFixedBuilder(positionCount, driverContext.blockFactory());
      MvMedian.Longs work = new MvMedian.Longs();
      for (int p = 0; p < positionCount; p++) {
        int valueCount = v.getValueCount(p);
        int first = v.getFirstValueIndex(p);
        long result = MvMedian.ascendingUnsignedLong(v, first, valueCount);
        builder.appendLong(result);
      }
      return Block.Ref.floating(builder.build().asBlock());
    }
  }
}
