package io.hashimati.microcli.domains;

/**
 * @author Ahmed Al Hashmi
 */
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.hashimati.microcli.utils.DataTypeMapper;
import io.hashimati.microcli.utils.Visitor;
import io.micronaut.core.util.StringUtils;

import static io.hashimati.microcli.constants.ProjectConstants.EntityAttributeType.*;
import static io.hashimati.microcli.constants.ProjectConstants.LanguagesConstants.*;

public class EntityAttribute {
    private String name, type, typePackage;
    private boolean premetive =true;
    private boolean enumuration = false;

    private boolean array=false;
    private boolean jdbc;
    private boolean jpa;

    private String backendDataRun;

    private EntityConstraints constraints;

    public EntityAttribute()
    {}
    @JsonIgnore
    public boolean isString(){

        return this.type.toString().trim().equalsIgnoreCase(STRING);
    }
    @JsonIgnore
    public boolean isInteger(){
        return this.type.toString().trim().equalsIgnoreCase(INTEGER);

    }
    @JsonIgnore
    public boolean isDate(){
        return this.type.toString().trim().equalsIgnoreCase(DATE);

    }
    @JsonIgnore
    public boolean isDouble(){
        return this.type.toString().trim().equalsIgnoreCase(DOUBLE);

    }
    @JsonIgnore
    public boolean isFloat(){

        return this.type.toString().trim().equalsIgnoreCase(FLOAT);

    }
    @JsonIgnore
    public boolean isByte(){
        return this.type.toString().trim().equalsIgnoreCase(BYTE);

    }
    @JsonIgnore
    public boolean isShort(){
        return this.type.toString().trim().equalsIgnoreCase(SHORT);

    }
    @JsonIgnore
    public boolean isLong(){
        return this.type.toString().trim().equalsIgnoreCase(LONG);

    }
    @JsonIgnore
    public boolean isChar(){
        return this.type.toString().trim().equalsIgnoreCase(CHAR);

    }
    @JsonIgnore
    public boolean isBigInteger()
    {
        return this.type.toString().trim().equalsIgnoreCase(BIG_INTEGER);

    }
    @JsonIgnore
    public boolean isBoolean()
    {
        return this.type.toString().trim().equalsIgnoreCase(BOOLEAN);

    }
    @JsonIgnore
    public boolean isBigDouble()
    {
        return this.type.toString().trim().equalsIgnoreCase(BIG_DECIMAL);

    }
    @JsonIgnore
    public boolean isClass()
    {
        return !isString()
                || !isPermetiveDataType();
    }
    @JsonIgnore
    private boolean isPermetiveDataType() {
        return isByte() || isInteger() || isShort() || isChar() || isDouble() || isFloat();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypePackage() {
        return typePackage;
    }

    public void setTypePackage(String typePackage) {
        this.typePackage = typePackage;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getType()
    {
        return this.type;
    }


    public String getDeclaration(String lang)
    {
        String constraintsDeclaration="";


        String elementCollectionAnnotation = "\t@ElementCollection(fetch = FetchType.EAGER)\n";
        if(array)
        {

            type = DataTypeMapper.wrapperMapper.getOrDefault(type.toLowerCase(), type);
            if(lang.equalsIgnoreCase(KOTLIN_LANG))
            {
                type = StringUtils.capitalize(type);
            }


            String collection;
            if(jdbc)
                collection = (lang.equalsIgnoreCase(KOTLIN_LANG)) ? "MutableSet" :"Set";
            else
                collection = collection=(lang.equalsIgnoreCase(KOTLIN_LANG)) ? "MutableList" :"List";

            type = collection +"<" +type +">";
        }
        switch (lang)
        {
            case JAVA_LANG:

                return ((jpa && array)?elementCollectionAnnotation:"")+"\tprivate " +type + " " + name +";\n";


            case GROOVY_LANG:
                return ((jpa && array)?elementCollectionAnnotation:"")+"\t" +type + " " + name +";\n";
            case KOTLIN_LANG:
                return ((jpa && array)?elementCollectionAnnotation:"")+"\t" +"var" + " " + name + ":" + StringUtils.capitalize(type) +"\n";
        }
        return "";
    }
    public String graphQLDeclaration()
    {
        String t = type;
        //return String in case of enum. 
        t = DataTypeMapper.graphqlMapper.containsKey(type) ? DataTypeMapper.graphqlMapper.get(type) : "String";
        return new StringBuilder().append("\t").append(name).append(" : ").append(t).append(",\n").toString();
    }
    public String getPackageSyntax(String lang)
    {
        if(this.getType().equalsIgnoreCase(BIG_DECIMAL))
        {
            return "import " + BIG_DECIMAL_CLASS + ";";
        }
        if(this.getType().equalsIgnoreCase(BIG_DECIMAL))
        {
            return "import " +BIG_INTEGER_CLASS + ";";
        }
        switch (lang)
        {
            case JAVA_LANG:
            case GROOVY_LANG:
            case KOTLIN_LANG:
                return "import " + getTypePackage() + ";";
        }
        return "";
    }

    public boolean isPremetive() {
        return premetive;
    }

    public void setPremetive(boolean premetive) {
        this.premetive = premetive;
    }

    public EntityConstraints getConstraints() {
        return constraints;
    }

    public void setConstraints(EntityConstraints constraints) {
        this.constraints = constraints;
    }

    @Override
    public String toString() {
        return "EntityAttribute{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", typePackage='" + typePackage + '\'' +
                ", premetive=" + premetive +
                ", array=" + array +
                ", constraints=" + constraints +
                '}';
    }

    public boolean isArray() {
        return array;
    }

    public void setArray(boolean array) {
        this.array = array;
    }

    public boolean isJpa() {
        return jpa;
    }

    public void setJpa(boolean jpa) {
        this.jpa = jpa;
    }


    public String getBackendDataRun() {
        return backendDataRun;
    }

    public void setBackendDataRun(String backendDataRun) {
        this.backendDataRun = backendDataRun;
    }

    public boolean isJdbc() {
        return jdbc;
    }

    public void setJdbc(boolean jdbc) {
        this.jdbc = jdbc;
    }
    public EntityAttribute visit(Visitor<EntityAttribute> visitor)
    {
        return visitor.visit(this);
    }

    public boolean isEnumuration() {
        return enumuration;
    }

    public void setEnumuration(boolean enumuration) {
        this.enumuration = enumuration;
    }
}
