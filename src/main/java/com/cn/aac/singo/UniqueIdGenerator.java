package com.cn.aac.singo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class UniqueIdGenerator {
    
    private static final int max = Integer.parseInt("zz", 36);
    private static volatile int seq = (new Random()).nextInt(max);
    private static SimpleDateFormat datePattern = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    
    /**
     * 做成唯一标识码。<br/>
     * 可以做成当前处理中唯一标识码。<br/>
     * <br/>
     * 做成的ID是时间和序列号组合构成。每调用一次这个方法，生成一个ID 做成的字符串为13位。<br/>
     * synchronized方法可以保证做成ID的唯一性。<br/>
     * 
     * @return ユニークＩＤ
     */
    public static synchronized String getUniqueId() {
        Date now = new Date();
        String dateFormat = Long.toString(Long.parseLong(datePattern.format(now)), 36);
        String sequence = sequencer();
        
        return dateFormat.concat(sequence);
    }
    
    /**
     * 取得序列后的值。序列后的值是每当这个方法被调用时会增加的值
     * 
     * @return 返还值是36进制数2位的字符串。
     */
    private static String sequencer() {
        
        if (seq == max) {
            seq = 0;
        } else {
            seq++;
        }
        
        // 根据位数，反悔36进制数据的处理
        if (seq < 36) {
            return ("0").concat(Integer.toString(seq, 36));
        } else {
            return Integer.toString(seq, 36);
        }
    }
}
