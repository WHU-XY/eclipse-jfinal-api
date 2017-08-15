package com.whu.jFinal.model;

public class ScenceDeviceModel {
		private String name;
		private String status;
		private String dev_id;
		private String channel_num;
		private String channel_id;
		public ScenceDeviceModel() {
			
		}
		public String getname() {
			return name;
		}
		public void setname(String name) {
			this.name=name;
		}
		public String getstatus() {
			return status;
		}
		public void setstatus(String status) {
			this.status=status;
		}
		public String getdev_id() {
			return dev_id;
		}
		public void setdev_id(String dev_id) {
			this.dev_id=dev_id;
		}
		public String getchannel_num() {
			return channel_num;
		}
		public void setchannel_num(String channel_num) {
			this.channel_num=channel_num;
		}
		public String getchannel_id() {
			return channel_id;
		}
		public void setchannel_id(String channel_id) {
			this.channel_id=channel_id;
		}
}
