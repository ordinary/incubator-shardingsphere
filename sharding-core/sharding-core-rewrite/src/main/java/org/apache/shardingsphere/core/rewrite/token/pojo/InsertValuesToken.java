/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.core.rewrite.token.pojo;

import lombok.Getter;
import org.apache.shardingsphere.core.rewrite.placeholder.Alterable;
import org.apache.shardingsphere.core.rewrite.placeholder.InsertValuePlaceholder;
import org.apache.shardingsphere.core.route.type.RoutingUnit;
import org.apache.shardingsphere.core.rule.DataNode;

import java.util.List;
import java.util.Map;

/**
 * Insert values token.
 *
 * @author maxiaoguang
 * @author panjuan
 */
@Getter
public final class InsertValuesToken extends SQLToken implements Substitutable, Alterable {
    
    private final int stopIndex;
    
    private final List<InsertValuePlaceholder> insertValues;
    
    public InsertValuesToken(final int startIndex, final int stopIndex, final List<InsertValuePlaceholder> insertValues) {
        super(startIndex);
        this.stopIndex = stopIndex;
        this.insertValues = insertValues;
    }
    
    @Override
    public String toString(final RoutingUnit routingUnit, final Map<String, String> logicAndActualTables) {
        StringBuilder result = new StringBuilder();
        appendUnits(routingUnit, result);
        result.delete(result.length() - 2, result.length());
        return result.toString();
    }
    
    private void appendUnits(final RoutingUnit routingUnit, final StringBuilder result) {
        for (InsertValuePlaceholder each : insertValues) {
            if (isToAppendInsertOptimizeResult(routingUnit, each)) {
                result.append(each).append(", ");
            }
        }
    }
    
    private boolean isToAppendInsertOptimizeResult(final RoutingUnit routingUnit, final InsertValuePlaceholder unit) {
        if (unit.getDataNodes().isEmpty() || null == routingUnit) {
            return true;
        }
        for (DataNode each : unit.getDataNodes()) {
            if (routingUnit.getTableUnit(each.getDataSourceName(), each.getTableName()).isPresent()) {
                return true;
            }
        }
        return false;
    }
}
