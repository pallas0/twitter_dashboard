package swim.tutorial;

import swim.api.ref.WarpRef;
import swim.structure.Record;
import java.util.HashSet;
import java.util.Set;
import com.twitter.clientlib.TwitterCredentialsBearer;
import com.twitter.clientlib.ApiException;
import com.twitter.clientlib.api.TwitterApi;
import com.twitter.clientlib.model.*;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
/**
 * Simple wrapper around some {@code SwimRef}, e.g. a {@code SwimClient} handle,
 * that pushes data to the Swim server running at {@code hostUri}.
 */
class DataSource {

  private final WarpRef ref;
  private final String hostUri;

  DataSource(WarpRef ref, String hostUri) {
    this.ref = ref;
    this.hostUri = hostUri;
  }

  void sendCommands() throws InterruptedException {
    int indicator = 0;
    while (true) {
      int foo = (int) (Math.random() * 10 - 5) + 30;
      int bar = (int) (Math.random() * 20 - 10) + 60;
      int baz = (int) (Math.random() * 30 - 15) + 90;
      if ((indicator / 25) % 2 == 0) {
        foo *= 2;
        bar *= 2;
        baz *= 2;
      }
      // msg's Recon serialization will take the following form:
      //   "{foo:$foo,bar:$bar,baz:$baz}"
      final Record msg = Record.create(3)
          .slot("foo", foo)
          .slot("bar", bar)
          .slot("baz", baz);

      // Push msg to the
      //   *CommandLane* addressable by "publish" OF the
      //   *Web Agent* addressable by "/unit/master" RUNNING ON the
      //   *(Swim) server* addressable by hostUri
      this.ref.command(this.hostUri, "/unit/master", "publish", msg);
      indicator = (indicator + 1) % 1000;

      // Throttle events to four every three seconds
      Thread.sleep(750);
    }
  }

  public static void main(String[] args) throws IOException {
    /**
     * Set the credentials for the required APIs.
     * The Java SDK supports TwitterCredentialsOAuth2 & TwitterCredentialsBearer.
     * Check the 'security' tag of the required APIs in https://api.twitter.com/2/openapi.json in order
     * to use the right credential object.
     */

    Properties props = new Properties();
    FileInputStream fis = new FileInputStream("config.properties");
    props.load(fis);
    fis.close();

    String apiKey = props.getProperty("api_key");
    String apiSecretKey = props.getProperty("api_secret_key");
    String bearerToken = props.getProperty("bearer_token");

    // System.out.println("API Key: " + apiKey);
    // System.out.println("API Secret Key: " + apiSecretKey);
    // System.out.println("Bearer Token: " + bearerToken);

    TwitterApi apiInstance = new TwitterApi(new TwitterCredentialsBearer(System.getenv(bearerToken)));

    Set<String> tweetFields = new HashSet<>();
    tweetFields.add("author_id");
    tweetFields.add("id");
    tweetFields.add("created_at");

    try {
     // findTweetById
     Get2TweetsIdResponse result = apiInstance.tweets().findTweetById("1646217242651025410")
      .tweetFields(tweetFields)
      .execute();
     if(result.getErrors() != null && result.getErrors().size() > 0) {
       System.out.println("Error:");

       result.getErrors().forEach(e -> {
         System.out.println(e.toString());
         if (e instanceof ResourceUnauthorizedProblem) {
           System.out.println(((ResourceUnauthorizedProblem) e).getTitle() + " " + ((ResourceUnauthorizedProblem) e).getDetail());
         }
       });
     } else {
       System.out.println("findTweetById - Tweet Text: " + result.toString());
     }
    } catch (ApiException e) {
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }

}
