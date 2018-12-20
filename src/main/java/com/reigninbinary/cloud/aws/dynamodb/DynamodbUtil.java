package com.reigninbinary.cloud.aws.dynamodb;

import java.util.Map;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.internal.InternalUtils;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public class DynamodbUtil {

	public static Item getItemFromAttrbuteMap(Map<String, AttributeValue> mapAttribtes) {
		
		// TODO: Internal AWS - keep an eye out.
		// this was deprectaed at some point but not anymore it seems.
		Map<String, Object> simpleMap = InternalUtils.toSimpleMapValue(mapAttribtes);
		
		return Item.fromMap(simpleMap);
	}
}
