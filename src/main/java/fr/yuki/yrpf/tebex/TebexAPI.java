package fr.yuki.yrpf.tebex;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.yuki.yrpf.tebex.responses.Command;
import fr.yuki.yrpf.tebex.responses.TebexCommandQueue;
import fr.yuki.yrpf.tebex.responses.TebexInformationResponse;
import net.onfirenetwork.onsetjava.Onset;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TebexAPI {
    private String secretKey;
    private String baseUrl = "https://plugin.tebex.io";

    public TebexAPI(String secretKey) throws Exception {
        this.secretKey = secretKey;
        TebexInformationResponse tebexInformationResponse = this.verifySecret();
        if(tebexInformationResponse == null) {
            throw new Exception("Can't initialize TebexAPI");
        }
        Onset.print("Tebex account is " + tebexInformationResponse.getAccount().getName() + " with currency " + tebexInformationResponse.getAccount().getCurrency().getSymbol());
    }

    private HttpURLConnection getTebexRequest(String path, String method) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(this.baseUrl + path).openConnection();
        con.setRequestMethod(method);
        con.setRequestProperty("X-Tebex-Secret", this.secretKey);
        con.setRequestProperty("user-agent", "Onset Server");
        return con;
    }

    private String readyBody(HttpURLConnection con) throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    private TebexInformationResponse verifySecret() throws IOException {
        HttpURLConnection request = this.getTebexRequest("/information", "GET");
        try {
            int responseCode = request.getResponseCode();
            String body = this.readyBody(request);
            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
            return gson.fromJson(body, TebexInformationResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public TebexCommandQueue getCommandQueues() throws IOException {
        HttpURLConnection request = this.getTebexRequest("/queue/offline-commands", "GET");
        try {
            int responseCode = request.getResponseCode();
            String body = this.readyBody(request);
            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
            return gson.fromJson(body, TebexCommandQueue.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteCommand(Command command) throws IOException {
        HttpURLConnection request = this.getTebexRequest("/queue?ids[]=" + command.getID(), "DELETE");
        try {
            int responseCode = request.getResponseCode();
            String body = this.readyBody(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
