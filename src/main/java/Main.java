import com.amazonaws.auth.profile.ProfilesConfigFile;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws Exception {
        final String tableName = "elg-dev-dynamostack-elgdevdynamostackusers106BDBCA-FVS3ZG6Q8VLR";
        String contactId = "0031X00000n323sQAA";
        if (args.length > 0) {
            contactId = args[0];
        }
        System.out.print("Insert aws profile name [default]: ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String profile = null;
        try {
            profile = br.readLine();
            if (profile.equals("")) {
                profile = "default";
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        ProfilesConfigFile pcf = new ProfilesConfigFile(new File(System.getProperty("user.home") + "/.aws/config"));
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.EU_WEST_1)
                .withCredentials(new StaticCredentialsProvider(pcf.getCredentials(profile)))
                .build();
        DynamoDB dynamoDB = new DynamoDB(client);
        Table table = dynamoDB.getTable(tableName);
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("contactId", contactId);
        Item outcome = table.getItem(spec);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        String json = gson.toJson(jp.parse(outcome.toString()));
        System.out.println(json);
    }
}
