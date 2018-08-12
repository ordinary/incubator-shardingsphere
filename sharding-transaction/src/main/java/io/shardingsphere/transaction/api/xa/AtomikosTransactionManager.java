/*
 * Copyright 2016-2018 shardingsphere.io.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package io.shardingsphere.transaction.api.xa;

import com.atomikos.icatch.jta.UserTransactionManager;
import io.shardingsphere.core.exception.ShardingException;
import io.shardingsphere.transaction.api.TransactionManager;
import io.shardingsphere.transaction.common.event.TransactionEvent;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import java.sql.SQLException;

/**
 * Atomikos XA transaction manager.
 *
 * @author zhaojun
 */
public final class AtomikosTransactionManager implements TransactionManager {
    
    private final static UserTransactionManager TRANSACTION_MANAGER = AtomikosUserTransaction.getInstance();
    
    static {
        try {
            TRANSACTION_MANAGER.init();
        } catch (final SystemException ex) {
            throw new ShardingException(ex);
        }
    }
    
    @Override
    public void begin(final TransactionEvent transactionEvent) throws SQLException {
        try {
            TRANSACTION_MANAGER.begin();
        } catch (final SystemException | NotSupportedException ex) {
            throw new SQLException(ex);
        }
    }
    
    @Override
    public void commit(final TransactionEvent transactionEvent) throws SQLException {
        try {
            TRANSACTION_MANAGER.commit();
        } catch (final RollbackException | HeuristicMixedException |HeuristicRollbackException | SystemException ex) {
            throw new SQLException(ex);
        }
    }
    
    @Override
    public void rollback(final TransactionEvent transactionEvent) throws SQLException {
        try {
            TRANSACTION_MANAGER.rollback();
        } catch (final SystemException ex) {
            throw new SQLException(ex);
        }
    }
    
    @Override
    public int getStatus() throws SQLException {
        try {
            return TRANSACTION_MANAGER.getStatus();
        } catch (final SystemException ex) {
            throw new SQLException(ex);
        }
    }
}