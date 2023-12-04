package org.acme;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.resource.spi.work.Work;
import jakarta.resource.spi.work.WorkCompletedException;
import org.jboss.tm.JBossXATerminator;

import javax.transaction.xa.XAException;
import javax.transaction.xa.Xid;

@ApplicationScoped
@Alternative
@Priority(Integer.MAX_VALUE)
public class NoOPXATerminator implements JBossXATerminator {
    @Override
    public void registerWork(final Work work, final Xid xid, final long timeout) throws WorkCompletedException {
    
    }
    
    @Override
    public void startWork(final Work work, final Xid xid) throws WorkCompletedException {
    
    }
    
    @Override
    public void endWork(final Work work, final Xid xid) {
    
    }
    
    @Override
    public void cancelWork(final Work work, final Xid xid) {
    
    }
    
    @Override
    public void commit(final Xid xid, final boolean onePhase) throws XAException {
    
    }
    
    @Override
    public void forget(final Xid xid) throws XAException {
    
    }
    
    @Override
    public int prepare(final Xid xid) throws XAException {
        return 0;
    }
    
    @Override
    public Xid[] recover(final int flag) throws XAException {
        System.out.println(NoOPXATerminator.class);
        return new Xid[0];
    }
    
    @Override
    public void rollback(final Xid xid) throws XAException {
    
    }
}
