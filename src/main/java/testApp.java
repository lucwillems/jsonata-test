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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class testApp {

    private static JsonNode getContext() throws IOException {
        try (FileReader fileReader = new FileReader("src/main/resources/data.json")) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(fileReader, JsonNode.class);
        }
    }
    public static void main(String[] args) throws IOException {
        Expressions expr = null;
        ObjectMapper mapper = new ObjectMapper();
        String expression = "child.name";
        JsonNode jsonObj=getContext();

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
