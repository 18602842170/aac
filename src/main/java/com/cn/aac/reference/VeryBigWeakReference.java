package com.cn.aac.reference;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * 弱引用扩展
 * @author Administrator
 *
 */
class VeryBigWeakReference extends WeakReference<VeryBig> {
    public String id;
    
    public VeryBigWeakReference(VeryBig big, ReferenceQueue<VeryBig> rq) {
        super(big, rq);
        this.id = big.id;
    }
    
    protected void finalize() {
        System.out.println("Finalizing VeryBigWeakReference " + id);
    }
}
