package me.runescapejon.main;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class Configuration {
	@Setting(value = "Message")
	public static String Message = "&6&lYour whole inventory was saved from your recent Death.";
}