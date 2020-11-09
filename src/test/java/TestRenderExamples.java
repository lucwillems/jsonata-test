
import com.api.jsonata4java.expressions.EvaluateException;
import com.api.jsonata4java.expressions.Expressions;
import com.api.jsonata4java.expressions.ParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;

public class TestRenderExamples {

    private Logger logger= LoggerFactory.getLogger(TestRenderExamples.class);

    private static JsonNode getFileContext() throws IOException {
        try (FileReader fileReader = new FileReader("src/main/resources/data.json")) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(fileReader, JsonNode.class);
        }
    }
    private JsonNode getContext() {
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

    public JsonNode evaluate(JsonNode ctx,String s) throws IOException, ParseException, EvaluateException {
        Expressions expr = Expressions.parse(s);
        JsonNode result = expr.evaluate(ctx);
        return result;
    }

    @Test
    public void testFileContext() throws Exception{
        JsonNode ctx = getFileContext();

        logger.info("{}",ctx.toString());
        logger.info("child: {}",evaluate(ctx,"child"));
        logger.info("child.name: {}",evaluate(ctx,"child.name"));
        logger.info("child.age: {}",evaluate(ctx,"child.age"));
        logger.info("child.parents: {}",evaluate(ctx,"child.parents"));
        logger.info("child.parents.name: {}",evaluate(ctx,"child.parents.name"));
    }

    @Test
    public void testContext() throws Exception{
        JsonNode ctx = getContext();

        logger.info("{}",ctx.toString());
        logger.info("child: {}",evaluate(ctx,"child"));
        logger.info("child.name: {}",evaluate(ctx,"child.name"));
        logger.info("child.age: {}",evaluate(ctx,"child.age"));
        logger.info("child.parents: {}",evaluate(ctx,"child.parents"));
        logger.info("child.parents.name: {}",evaluate(ctx,"child.parents.name"));
    }
}
