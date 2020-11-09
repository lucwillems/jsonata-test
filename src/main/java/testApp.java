import com.api.jsonata4java.expressions.EvaluateException;
import com.api.jsonata4java.expressions.EvaluateRuntimeException;
import com.api.jsonata4java.expressions.Expressions;
import com.api.jsonata4java.expressions.ParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

public class testApp {

    private static JsonNode getContext() {
        ObjectNode data = JsonNodeFactory.instance.objectNode();
        data.put("name","jos");
        data.put("age",10);
        ArrayNode parents=JsonNodeFactory.instance.arrayNode();
        data.putPOJO("parents",parents);
        ObjectNode father = JsonNodeFactory.instance.objectNode();
        father.put("name","father");
        father.put("age",60);
        parents.add(father);
        ObjectNode mother = JsonNodeFactory.instance.objectNode();
        mother.put("name","mother");
        mother.put("age",50);
        parents.add(mother);
        ObjectNode child=JsonNodeFactory.instance.objectNode();
        child.putPOJO("child",data);
        return child;
    }
    public static void main(String[] args) {
        Expressions expr = null;
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonObj = null;
        String json = "{ \"a\":1, \"b\":2, \"c\":[1,2,3,4,5] }";
        String expression = "$child.name";
        try {
            jsonObj = mapper.readTree(json);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        jsonObj=getContext();

        try {
            System.out.println("Using json:\n" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObj));
            System.out.println("expression=" + expression);
            expr = Expressions.parse(expression);
        } catch (ParseException e) {
            System.err.println(e.getLocalizedMessage());
        } catch (EvaluateRuntimeException ere) {
            System.out.println(ere.getLocalizedMessage());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            System.out.println("evaluate returns:");
            JsonNode result = expr.evaluate(jsonObj);
            if (result == null) {
                System.out.println("** no match **");
            } else {
                System.out.println("" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));
            }
        } catch (EvaluateException | JsonProcessingException e) {
            System.err.println(e.getLocalizedMessage());
        }
    }
}
