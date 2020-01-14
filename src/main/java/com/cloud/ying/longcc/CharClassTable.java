package com.cloud.ying.longcc;

import com.cloud.ying.longcc.regular.RegularExpression;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CharClassTable {
    public CharClassTable(Map<Integer, Set<Character>> classTable,Integer maxClass) {
        this.classTable = classTable;
        this.maxClass = maxClass;
    }

    Map<Integer, Set<Character>> classTable;
    Integer maxClass;

    public Integer GetClass(Character character){
        Iterator iterator = classTable.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Integer, Set<Character>> item = (Map.Entry<Integer, Set<Character>>)iterator.next();

            if(item.getValue().contains(character)){
                return item.getKey();
            }
        }
        return -1;
    }
}
