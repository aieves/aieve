package ai.eve.volley.request;

import java.io.File;
import java.util.Map;

/**
 * @author ZhiCheng Guo
 * @version 2014年10月7日 上午11:04:36
 */
public interface MultiPartRequest {
    public Map<String,File> getFiles();
}