package com.json;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
 
public class TimeSerializer implements JsonSerializer<Date>
{
   private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
   public JsonElement serialize(Date date, Type typeOfSrc, JsonSerializationContext context)
   {
      return new JsonPrimitive(dateFormat.format(date));
   }
}