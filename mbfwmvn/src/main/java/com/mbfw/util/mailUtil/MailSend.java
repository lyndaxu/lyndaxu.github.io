package com.mbfw.util.mailUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

import com.mbfw.entity.system.User;
import com.mbfw.util.ArrayUtil;
import com.mbfw.util.DateUtil;
import com.mbfw.util.StringUtil;
import com.mbfw.util.Tools;






public class MailSend {
	public static String toChinese(String text) {
		try {
			text = MimeUtility.encodeText(new String(text.getBytes(), "UTF-8"),
					"GB2312", "B");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return text;
	}

	/**
	 * 简单的邮件发送
	 * 
	 * @param to
	 *            主送人
	 * @param subject
	 *            邮件主题
	 * @param value
	 *            邮件内容
	 * @throws Exception
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static boolean sendMail(MailSendInfo mailInfo) throws Exception {
		return sendMail(mailInfo, null, null);
	}

	/**
	 * 带多附件的邮件发送
	 * 
	 * @param to
	 *            主送人
	 * @param subject
	 *            邮件主题
	 * @param value
	 *            邮件内容
	 * @param attachments
	 *            文件
	 * @param streamAttachments
	 *            文件流
	 * @throws Exception
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static boolean sendMail(MailSendInfo mailInfo,
			Map<String, File> attachments,
			Map<String, ByteArrayOutputStream> streamAttachments)
			throws Exception {
		// 判断是否需要身份认证
		MyAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		if (mailInfo.isValidate()) {
			// 如果需要身份认证，则创建一个密码验证器
			authenticator = new MyAuthenticator(mailInfo.getUserName(),
					mailInfo.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session
				.getDefaultInstance(pro, authenticator);
		try {
			// 创建邮件发送者地址
			String from = mailInfo.getFromAddress();
			// 创建邮件的接收者地址，并设置到邮件消息中
			HashSet<?> toAddress = mailInfo.getToAddress();
			InternetAddress[] to = null;
			to = iteratorHashSet(toAddress);
			HashSet<?> ccAddress = mailInfo.getCcAddress();
			InternetAddress[] cc = null;
			if (!ArrayUtil.isEmpty(ccAddress)) {
				cc = iteratorHashSet(ccAddress);
			}
			HashSet<?> bccAddress = mailInfo.getBccAddress();
			InternetAddress[] bcc = null;
			if (!ArrayUtil.isEmpty(bccAddress)) {
				bcc = iteratorHashSet(bccAddress);
			}

			boolean sendMail = sendMail(sendMailSession, from, to, cc, bcc,
					mailInfo.getSubject(), mailInfo.getContent(), attachments,
					streamAttachments);

			return sendMail;
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
		return false;
	}
	private static InternetAddress[] iteratorHashSet(HashSet<?> set) throws Exception {
		List<InternetAddress> email = new ArrayList<InternetAddress>();
		for(Object obj : set ) {
			 if (User.class.isInstance(obj)) {
				 if(Tools.isEmpty(((User) obj).getNAME())){
					 email.add(new InternetAddress(((User) obj).getSKIN()));
				 }
			 } else if (String.class.isInstance(obj)) {
				 	email.add(new InternetAddress((String) obj));
			 } else {
					throw new Exception(
							"集合的泛型无法取到邮件地址，泛型类型必须是User或者String类型");
			 }
		}
		InternetAddress[] address = (InternetAddress[])email.toArray(new InternetAddress[email.size()]);
		return address;
	}

	private static boolean sendMail(Session sendMailSession,
			String fromAddress, InternetAddress[] to, InternetAddress[] cc,
			InternetAddress[] bcc, String subject, String content,
			Map<String, File> attachments,
			Map<String, ByteArrayOutputStream> streamAttachments)
			throws UnsupportedEncodingException {

		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress(fromAddress);
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			// 创建邮件的接收者地址，并设置到邮件消息中
			mailMessage.setRecipients(Message.RecipientType.TO, to);
			if (cc != null && cc.length > 0) {
				mailMessage.setRecipients(Message.RecipientType.CC, cc);
			}
			if (bcc != null && bcc.length > 0) {
				mailMessage.setRecipients(Message.RecipientType.BCC, bcc);
			}
			// 设置邮件消息的主题
			mailMessage.setSubject(subject);

			// 邮件所带的内容（包括正文或者附件等）
			Multipart mp = new MimeMultipart();

			// 添加正文
			MimeBodyPart mbpContent = new MimeBodyPart();
			//content = "<center><table border=\"1\"  style='border-collapse: collapse;' ><h2>已覆盖客户情况</h2><tr><th style='background-color:#CCC;padding:5px 5px 5px 5px;'>测试1电饭锅</th><th style='background-color:#CCC;padding:5px 5px 5px 5px;'>测试2地方如果他还有距</th></tr><tr><td>dsfsgrtdhnyjumti你开完会岁IE从而创建 </td><td></td></tr></table><center>";
			// 设置邮件消息的主要内容
			content = content.replace("&abc;", "<").replace("&cba;", ">")
					.replace("&quot;", "\"").replace("&#39;", "'")
					.replace("&nbsp;", " ").replace("&lt;", "<")
					.replace("&gt;", ">");
			mbpContent.setContent(content, "text/html;charset=UTF-8");
			mp.addBodyPart(mbpContent);

			/* 往邮件中添加附件 */
			if (attachments != null && attachments.size() > 0) {
				for (String key : attachments.keySet()) {
					MimeBodyPart mbpFile = new MimeBodyPart();
					FileDataSource fds = new FileDataSource(
							attachments.get(key));
					mbpFile.setDataHandler(new DataHandler(fds));
					mbpFile.setFileName(MimeUtility.encodeWord(fds.getName()));
					mp.addBodyPart(mbpFile);
				}
			}
			if (streamAttachments != null && streamAttachments.size() > 0) {
				for (String key : streamAttachments.keySet()) {
					MimeBodyPart mbpFile = new MimeBodyPart();
					ByteArrayOutputStream baos = streamAttachments.get(key);
					ByteArrayDataSource byteArrayDataSource = new ByteArrayDataSource(
							baos.toByteArray(),key);
					mbpFile.setDataHandler(new DataHandler(byteArrayDataSource));
					mbpFile.setFileName(MimeUtility
							.encodeWord(byteArrayDataSource.getName()));
					mp.addBodyPart(mbpFile);
				}
			}
			mailMessage.setContent(mp);
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(DateUtil.getNow());
			// 发送邮件
			Transport.send(mailMessage);
			return true;
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public static void main(String[] args) throws Exception {
		// 这个类主要是设置邮件
		MailSendInfo mailInfo = new MailSendInfo();
		//hashSet可以去除重复的邮件地址
		HashSet<String> to = new HashSet<String>();
		to.add("ex_liuze@citics.com");
		to.add("ex_liuze@citics.com");
		mailInfo.setToAddress(to);
		mailInfo.setSubject("设置邮箱标题");
		mailInfo.setContent("设置邮箱内容");
		Map<String, File> map = new HashMap<String, File>();
		File f = new File(
				"C:/Users/Administrator/Desktop/投行邮件发送功能统计-刘泽20171212.xlsx");
		map.put("投行邮件发送功能统计-刘泽201712", f);
		// 这个类主要来发送邮件
		MailSendUtils.send(mailInfo, map, null,
				MailSendUtils.YES_ATTACHMENT_MAIL);// 发送文体格式
	}
}
