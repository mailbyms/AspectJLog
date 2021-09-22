
package com.gyjian.logall.controller;

import com.gyjian.logall.annotation.SysLog;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author
 */
@RestController
@RequestMapping("/p")
public class TestController {
    /**
	 * 分页获取
	 */
	@SysLog("获取什么信息")
    @GetMapping("/test")
	public String getByPid(@RequestParam("pid") Long pid){
		return "OK";
	}

}
