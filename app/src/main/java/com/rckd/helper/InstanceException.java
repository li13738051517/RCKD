package com.rckd.helper;

/**
 * An {@link RuntimeException} thrown in cases something went wrong inside Tower.
 * Created by LiZheng on 17/2/5.
 */
public class InstanceException extends RuntimeException {
    public InstanceException(String detailMessage) {
        super(detailMessage);
    }
}
