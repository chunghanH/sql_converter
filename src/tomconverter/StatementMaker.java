package tomconverter;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class StatementMaker {
    String statement_type,variable_type, variable_name, text, table_name;
    String [] columns;
    StringBuffer sb = new StringBuffer();
    
    public StatementMaker(String st, String vt, String vn, String text, String tn){
        this.statement_type = st;
        this.variable_type = vt;
        this.variable_name = vn;
        this.text = text;
        this.table_name = tn;
        this.columns = getSpliterArray(text);
        go();
    }
    
    public void setSelect(){
        if(variable_type.equals("String")){
            sb.append("String "+variable_name+"=\"select \";\n");
            sb.append(getColumnsName());
            sb.append(variable_name+"+=\"from "+table_name+" \";\n");
        }else if(variable_type.equals("StringBuffer")){
            sb.append("StringBuffer "+variable_name+" = new StringBuffer();\n");
            sb.append(variable_name+".append(\"select \");\n");
            sb.append(getColumnsName());
            sb.append(variable_name+".append(\"from "+table_name+" \");\n");
        }
    }
    
    public void setInsert(){
        if(variable_type.equals("String")){
            sb.append("String "+variable_name+"=\"insert into "+table_name+" (\";\n");
            sb.append(getColumnsName());
            sb.append(variable_name+"+=\") values (\";\n");
            for(int i=0;i<columns.length;i++){
                if(i==columns.length-1){
                    sb.append(variable_name+"+=\"'\"++\"') \";  //"+i+"\n");
                }
                else{
                    sb.append(variable_name+"+=\"'\"++\"',\";  //"+i+"\n");
                }
            }
        }else if(variable_type.equals("StringBuffer")){
            sb.append("StringBuffer "+variable_name+" = new StringBuffer();\n");
            sb.append(variable_name+".append(\"insert into "+table_name+" (\");\n");
            sb.append(getColumnsName());
            sb.append(variable_name+".append(\") values (\");\n");
            for(int i=0;i<columns.length;i++){
                if(i==columns.length-1){
                    sb.append(variable_name+".append(\"'\"++\"')\");  //"+i+"\n");
                }
                else{
                    sb.append(variable_name+".append(\"'\"++\"',\");  //"+i+"\n");
                }
            }
        }
    }
    
    public void setUpdate(){
        if(variable_type.equals("String")){
            sb.append("String "+variable_name+"=\"update set "+table_name+" \";\n");
            for(int i=0;i<columns.length;i++){
                if(i==columns.length-1){
                    sb.append(variable_name+"+=\""+columns[i]+"='\"++\"' \";  //"+i+"\n");
                }
                else{
                    sb.append(variable_name+"+=\""+columns[i]+"='\"++\"',\";  //"+i+"\n");
                }
            }
        }else if(variable_type.equals("StringBuffer")){
            sb.append("StringBuffer "+variable_name+" = new StringBuffer();\n");
            sb.append(variable_name+".append(\"update set "+table_name+" \");\n");
            for(int i=0;i<columns.length;i++){
                if(i==columns.length-1){
                    sb.append(variable_name+".append(\""+columns[i]+"='\"++\"' \");  //"+i+"\n");
                }
                else{
                    sb.append(variable_name+".append(\""+columns[i]+"='\"++\"',\");  //"+i+"\n");
                }
            }
        }
    }
    
    public void setDelete(){
        if(variable_type.equals("String")){
            sb.append("String "+variable_name+"=\"delete from "+table_name+" \";\n");
        }else if(variable_type.equals("StringBuffer")){
            sb.append("StringBuffer "+variable_name+" = new StringBuffer();\n");
            sb.append(variable_name+".append(\"delete from "+table_name+" \");\n");
        }
    }
    
    public String[] getSpliterArray(String text){
        Vector vc = new Vector();
        Pattern p = Pattern.compile("[a-zA-Z0-9]*");
        
        String[] lines = text.split("\\n");
        for(int i=0;i<lines.length;i++){
           lines[i] = lines[i].trim().replaceAll(" +","-");
           String tmp[] = lines[i].split("-");
           for(int j=0;j<tmp.length;j++){
                Matcher matcher = p.matcher(tmp[j]);
                if(matcher.matches()){
                     vc.add(tmp[j]);
                     break;
                }
                else{
                    break;
                }   
           }
        }
        String result[] = (String[])vc.toArray(new String[vc.size()]);
        vc.clear();
        return result;
    }
    
    public String getColumnsName(){
        StringBuffer content = new StringBuffer();
        if(variable_type.equals("String")){
            for(int i=0;i<columns.length;i++){
                //System.out.println(columns[i]);
                if(i==columns.length-1){
                    content.append(variable_name+"+=\""+columns[i]+" \";  //"+i+"\n");
                }
                else{
                    content.append(variable_name+"+=\""+columns[i]+",\";  //"+i+"\n");
                }
            }
        }else if(variable_type.equals("StringBuffer")){
            for(int i=0;i<columns.length;i++){
                if(i==columns.length-1){
                    content.append(variable_name+".append(\""+columns[i]+" \");  //"+i+"\n");
                }
                else{
                    content.append(variable_name+".append(\""+columns[i]+",\");  //"+i+"\n");
                }
            }
        }
        String result = content.toString();
        content.setLength(0);
        return result;
    }
    
    public void go(){
        if(statement_type.equals("SELECT")){
            setSelect();
        }else if(statement_type.equals("INSERT")){
            setInsert();
        }else if(statement_type.equals("UPDATE")){
            setUpdate();
        }else if(statement_type.equals("DELETE")){
            setDelete();
        }
    }
    
    public String getStatement(){
        String result = sb.toString();
        sb.setLength(0);
        return result;
    }
}
