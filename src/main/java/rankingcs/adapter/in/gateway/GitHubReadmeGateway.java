package rankingcs.adapter.in.gateway;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GitHubReadmeGateway {

    private final RestTemplate restTemplate;

    public GitHubReadmeGateway(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String fetchReadme(String url) {
        return restTemplate.getForObject(url, String.class);
    }

    public List<String> fetchMdFiles(String repoUrl) {
        String apiUrl = repoUrl.replace("https://github.com/", "https://api.github.com/repos/") + "/contents";
        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {
                });
        return response.getBody().stream()
                .filter(file -> file.get("name").toString().endsWith(".md") && file.get("name").toString().startsWith("standing"))
                .map(file -> file.get("download_url").toString())
                .collect(Collectors.toList());
    }
}