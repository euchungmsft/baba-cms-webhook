package com.eg.baba.cms.webhook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eg.baba.cms.alert.CMSAlert;
import com.eg.baba.cms.alert.CMSAlertMessage;


@RestController
public class CMSWebhookController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private JmsTemplate jmsTemplate;

	@RequestMapping("/")
	public String index() {
		return "Callback for Alibaba Cloud CMS";
	}

	@RequestMapping(value = "/callback", method = RequestMethod.GET)
	public String info() {
		return "Callback for CMS event";
	}

	@RequestMapping(value = "/callback", method = RequestMethod.POST)
	public String runCallback(@RequestBody String body, @RequestHeader HttpHeaders head) {

		// log.trace("key ["+key+"]");
		log.trace("head [" + head + "]");
		log.trace("body [" + body + "]");

		CMSAlertMessage msg = CMSAlertMessage.fromString(body);
		log.info(msg.toString());

		try {
			jmsTemplate.convertAndSend(JMSConfig.ALERT_QUEUE, new CMSAlert(0, msg));
		} catch (Throwable th) {
			th.printStackTrace();
		}

		return "OK";

	}

	public static void main(String args[]) {

	}

}
