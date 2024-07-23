//package rankingcs.application.service.impl;
//
//import org.springframework.stereotype.Component;
//import rankingcs.adapter.in.gateway.GitHubReadmeGateway;
//
//import java.util.List;
//
//@Component
//public class SaveRankingFromContentService {
//
//    private final GitHubReadmeGateway gitHubReadmeGateway;
//    private final ConvertFileToJsonService saveRankingFromContent;
//
//    public SaveRankingFromContentService(final GitHubReadmeGateway gitHubReadmeGateway, final ConvertFileToJsonService saveRankingFromContent) {
//        this.gitHubReadmeGateway = gitHubReadmeGateway;
//        this.saveRankingFromContent = saveRankingFromContent;
//    }
//
//    public String saveReadmeFiles() {
//        String repoUrl = "https://github.com/ValveSoftware/counter-strike_regional_standings";
//        List<String> mdFiles = gitHubReadmeGateway.fetchMdFiles(repoUrl);
//        for (String mdFileUrl : mdFiles) {
//            String content = gitHubReadmeGateway.fetchReadme(mdFileUrl);
//            saveRankingFromContent.saveRankingFromContent(content);
//        }
//        return "N-Ok";
//    }
//}
