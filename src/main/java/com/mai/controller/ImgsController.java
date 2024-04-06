package com.mai.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mai.common.lang.Result;
import com.mai.entity.Imgs;
import com.mai.service.ImgsService;
import com.mai.util.MyFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class ImgsController {
    @Autowired
    ImgsService imgsService;

    /**
     * 【游客】查看商品评论
     */
    @PostMapping("/imgsCmtList")
    public Result imgsCmtList(@RequestBody List<Long> imgsIdList) {
        Map<Long, List<Imgs>> map = new HashMap<>();
        for (Long imgsId : imgsIdList) {
            if (imgsId != null) {
                QueryWrapper<Imgs> wrapper = new QueryWrapper<Imgs>().eq("imgs_id", imgsId);
                map.put(imgsId, imgsService.list(wrapper));
            }
        }
        return Result.succ(map);
    }

    /**
     * 测试用的demo
     */
    @PostMapping("/upload")
    public Result upload(@RequestParam("files") MultipartFile[] files) throws IOException {
        Long imgsId = MyFileUtil.uploadFiles(files, "test");
        return Result.succ(imgsId);
    }



    @GetMapping("/getFile")
    public void getFile(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        //读取路径下面的文件
        String filePath = request.getParameter("filePath");
        String name = request.getParameter("name");
        filePath = "D:" + filePath.substring(23);
        String suffix = filePath.substring(filePath.lastIndexOf("."));
        System.err.println("文件来源：" + filePath);
        System.err.println("文件下载名：" + name + suffix);

        File file = new File(filePath);
        response.setHeader("Content-disposition", "attachment; filename="
                + URLEncoder.encode(name, "utf-8") + suffix);
        //读取指定路径下面的文件
        InputStream in = new FileInputStream(file);
        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        //创建存放文件内容的数组
        byte[] buff =new byte[1024];
        //所读取的内 容使用n来接收
        int n;
        //当没有读取完时,继续读取,循环
        while((n=in.read(buff))!=-1){
            //将字节数组的数据全部写入到输出流中
            outputStream.write(buff,0,n);
        }
        //强制将缓存区的数据进行输出
        outputStream.flush();
        //关流
        outputStream.close();
        in.close();
    }

}
