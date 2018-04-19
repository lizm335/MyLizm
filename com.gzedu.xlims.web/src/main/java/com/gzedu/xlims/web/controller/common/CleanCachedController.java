package com.gzedu.xlims.web.controller.common;

import javax.annotation.Resource;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.web.common.Feedback;

@Controller
@RequestMapping("/cleanCached")
public class CleanCachedController {

	@Resource(name = "redisTemplate")
	private HashOperations<String, String, Object> hashOps;

	@RequestMapping(value = "form")
	public String form() {
		return "cleanCached";
	}

	@RequestMapping(value = "cleanByKey")
	@ResponseBody
	public Feedback cleanByKey(String key) {
		Feedback fb = new Feedback(true, "删除成功");
		hashOps.delete("Dictionary", key);
		return fb;
	}

	@RequestMapping(value = "cleanAll")
	@ResponseBody
	public Feedback cleanAll() {
		Feedback fb = new Feedback(true, "删除成功");
		hashOps.delete("Dictionary");
		return fb;
	}

	@RequestMapping(value = "getValueByKey")
	@ResponseBody
	public Feedback getValueByKey(String key) {
		Feedback fb = new Feedback(true, "删除成功");
		hashOps.get("Dictionary", key);
		return fb;
	}
}
