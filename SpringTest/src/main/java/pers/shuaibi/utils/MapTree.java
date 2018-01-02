package pers.shuaibi.utils;



import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author 宋帅比 
 *
 */
public class MapTree {
    private Map<String, String> child_parent = new HashMap<String, String>(); //key:子节点  value:父节点
    private Map<String, Set<String>> parent_child = new HashMap<String, Set<String>>();//key:父节点  value:子集合
      
    public void add(String parent,String child){  
        child_parent.put(child,parent);  
        Set<String> childs = (Set<String>)parent_child.get(parent);  
        if(childs==null)  
        {  
        	childs = new HashSet<String>();  
            parent_child.put(parent, childs);  
        }  
        childs.add(child);  
    }  
      
    public String getParent(String key){  
        return (String) child_parent.get(key);  
    }  
      
    public Set<String> getChild(String key){  
        return (Set<String>)parent_child.get(key);  
    }
    
    /**
     * 递归查询传入节点及其子节点的叶节点
     * @param x 节点
     * @param set 集合存放叶节点
     * @return 返回叶节点集合
     */
    public  Set<String> getLeafNodes(String x,Set<String> set) {
    	if(this.getChild(x)!=null&&!this.getChild(x).isEmpty()) {
    		for(String s:this.getChild(x)) {
    			this.getLeafNodes(s,set);
    		}
    	}else {
    		if(this.getParent(x)!=null&&this.getParent(x).length()>0) {
    			set.add(x);
    		}
    		
    	}
		return set;
    }
    
    /**
     * 查询子叶节点
     * @param x
     * @return
     */
    public  Set<String> getChildLeafNodes(String x) {
    	Set<String> childs = this.getChild(x);
    	if(childs!=null&&!childs.isEmpty()) {
    		for(String s:childs) {
    			if(this.getChild(s)!=null) {
    				childs.remove(s);
    			}
    		}
    	}else {
    		if(this.getParent(x)!=null&&this.getParent(x).length()>0) {
    			childs = new HashSet<String>();
    			childs.add(x);
    		}
    	}
    	
		return childs;
    }
    
}
