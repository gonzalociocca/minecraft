package server.command.server.edit.classeditor;

//TODO: Fill with Class Type

import server.command.server.edit.classeditor.editortype.*;

import java.lang.reflect.Field;

public enum ClassType {
    Boolean(Boolean.class, new BooleanEditor()),
    Object(null, new ObjectEditor()),
    Double(Double.class, new DoubleEditor()),
    Integer(Integer.class, new IntegerEditor()),
    Long(Long.class, new LongEditor()),
    String(String.class, new StringEditor()),
    List(java.util.List.class, new ListEditor()),
    Set(java.util.Set.class, new SetEditor())
    ;

    Class _type;
    ClassEditor _classEditor;

    ClassType(Class type, ClassEditor classEditor){
        _type = type;
        _classEditor = classEditor;
    }

    public Class getType(){
        return _type;
    }

    public ClassEditor getEditor(){
        return _classEditor;
    }

    public static ClassType getByObject(Object object){
        return getByClass(object.getClass());
    }

    public static ClassType getByField(Object origin, Field field){
        try {
            return getByClass(field.get(origin).getClass());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ClassType getByClass(Class type){
        for(ClassType classType : ClassType.values()){
            if(classType.getType() != null && classType.getType().isAssignableFrom(type)){
                return classType;
            }
        }
        return ClassType.Object;
    }
}
