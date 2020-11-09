
import com.api.jsonata4java.expressions.EvaluateException;
import com.api.jsonata4java.expressions.Expressions;
import com.api.jsonata4java.expressions.ParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;

public class TestRenderExamples {

    private Logger logger= LoggerFactory.getLogger(TestRenderExamples.class);
    ObjectMapper mapper = new ObjectMapper();

    private JsonNode getFileContext() throws IOException {
        try (FileReader fileReader = new FileReader("src/main/resources/data.json")) {
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

    private JsonNode getWithSetContext() {
        ObjectNode data = JsonNodeFactory.instance.objectNode();
        data.put("name","jos");
        data.put("age",10);
        ArrayNode parents=JsonNodeFactory.instance.arrayNode();
        data.set("parents",parents);
        ObjectNode father = JsonNodeFactory.instance.objectNode();
        father.put("name","father");
        father.put("age",60);
        parents.add(father);
        ObjectNode mother = JsonNodeFactory.instance.objectNode();
        mother.put("name","mother");
        mother.put("age",50);
        parents.add(mother);
        ObjectNode child=JsonNodeFactory.instance.objectNode();
        child.set("child",data);
        return child;
    }
    public JsonNode evaluate(JsonNode ctx,String s) throws IOException, ParseException, EvaluateException {
        Expressions expr = Expressions.parse(s);
        JsonNode result = expr.evaluate(ctx);
        return result;
    }

    private void checkIt( JsonNode json) throws IOException, ParseException, EvaluateException {
        logger.info("{}",mapper.writeValueAsString(json));
        logger.info("child: {}",evaluate(json,"child"));
        logger.info("child.name: {}",evaluate(json,"child.name"));
        logger.info("child.age: {}",evaluate(json,"child.age"));
        logger.info("child.parents: {}",evaluate(json,"child.parents"));
        logger.info("child.parents.name: {}",evaluate(json,"child.parents.name"));
        Assert.assertNotNull(evaluate(json,"child"));
        Assert.assertNotNull(json.path("child").path("name"));
        Assert.assertNotNull(evaluate(json,"child.name"));
        Assert.assertNotNull(evaluate(json,"child.parents"));
        Assert.assertNotNull(evaluate(json,"child.parents.name"));

    }

    @Test
    public void testFileContext() throws Exception{
        JsonNode json = getFileContext();
        checkIt(json);
    }

    @Test
    public void testContext() throws Exception{
        JsonNode json = getContext();
        checkIt(json);
    }

    @Test
    public void testSetContext() throws Exception{
        JsonNode json = getWithSetContext();
        checkIt(json);
    }
}
