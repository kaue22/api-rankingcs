package rankingcs.adapter.in.persistence.dto;

import org.springframework.data.annotation.Id;

public class ReadmeDto {
    @Id
    private String id;
    private String content;
    private String creationDate;

    public ReadmeDto(String id, String content, String creationDate) {
        this.id = id;
        this.content = content;
        this.creationDate = creationDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
}
