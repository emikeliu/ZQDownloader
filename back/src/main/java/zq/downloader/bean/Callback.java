package zq.downloader.bean;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor

public class Callback {

    private String status;
    private String taskName;
    private String url;
    private String uuid;
    private String success;
    private String msg;
    private Double percent;
    private Long size;
    private String sha256;

    private Boolean shared;

}
