package biz.letsweb.tasker.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author toks
 */
public class DependencyModel {

    private String tag;
    private int id;
    private final Map<DependencyModel, List<DependencyModel>> models;

    public DependencyModel() {
        models = new HashMap<>();
        
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addChild(DependencyModel model) {
//        System.out.println(this + "--" + model);
        if(models.containsKey(this)){
            models.get(this).add(model);
            System.out.println(this + "adding: " + model);
        }else{
            models.put(this, new ArrayList<DependencyModel>());
        }
            
    }
    public List<DependencyModel> getChildren(){
        return models.remove(this);
    }
    public Map<DependencyModel, List<DependencyModel>> getModels() {
        return models;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DependencyModel other = (DependencyModel) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DependencyModel{" + "tag=" + tag + ", id=" + id + '}';
    }

}
