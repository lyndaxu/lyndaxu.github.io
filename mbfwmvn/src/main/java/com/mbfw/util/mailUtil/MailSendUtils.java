package com.mbfw.util.mailUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 邮件发送工具类<br>
 * 包括简单邮件和多附件邮件发送，根据你发送的实际需要传入参数
 * @author by newze
 *
 */
public class MailSendUtils {
	/**
	 * 是否发送带附件的邮件的常量,是带有附件
	 */
	public final static String YES_ATTACHMENT_MAIL = "Y";
	/**
	 * 是否发送带附件的邮件的常量,不带有附件，简单邮件发送
	 */
	public final static String NO_ATTACHMENT_MAIL = "N";
	// 创建固定数目线程的线程池。FixedThreadPool适用于为了满足管理资源的需求，而需要限制当前线程数量的应用场景，它适用于负载比较重的服务器。
	private static Executor executor = Executors.newFixedThreadPool(10);
	/**
	 * 
	 * @param mailInfo 邮件的基本信息
	 * @param attachments 多附件，多附件以不同的key值放到Map容器中，再发送
	 * @param streamAttachments 附件流形式，多附件流以不同的key值放到Map容器中，再发送
	 * @param contentType 这个是是否带有附件的标识，<br><li>MailSendUtils.YES_ATTACHMENT_MAIL="Y"表示有附件 </li>
	 *  <br> <li>MailSendUtils.NO_ATTACHMENT_MAIL="N"表示不带附件 </li>
	 */
	public static void send(final MailSendInfo mailInfo,
			final Map<String, File> attachments,
			final Map<String, ByteArrayOutputStream> streamAttachments,
			final String contentType) {
		Runnable task = new Runnable() {
			@Override
			public void run() {
				try {
					if (YES_ATTACHMENT_MAIL.equals(contentType)) {
						MailSend.sendMail(mailInfo, attachments,
								streamAttachments);
					} else if(NO_ATTACHMENT_MAIL.equals(contentType)) {
						MailSend.sendMail(mailInfo);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		};
		executor.execute(task);
	}
}
