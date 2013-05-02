package com.jwxicc.cricket.jsf.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.el.ELContext;
import javax.el.ListELResolver;

import com.jwxicc.cricket.entity.Fow;
import com.jwxicc.cricket.entity.Inning;

public class SetToListELResolver extends ListELResolver {

	public static final String KEY_PROPERTY = "setToList";

	@Override
	public Object getValue(ELContext context, Object base, Object property) {
		if (base instanceof Set<?> && KEY_PROPERTY.equals(property)) {
			context.setPropertyResolved(true);
			List<Object> list = new ArrayList<Object>((Set<?>) base);
			
			// If it is a list of inns, sort by inns of match
			if (list.get(0) instanceof Inning) {
				Collections.sort(list, new Comparator<Object>() {
					@Override
					public int compare(Object o1, Object o2) {
						// orders innings by innings of match
						return Integer.valueOf(((Inning)o1).getInningsOfMatch()).compareTo(
								Integer.valueOf(((Inning)o2).getInningsOfMatch()));
					}
				});
			}
			
			if (list.get(0) instanceof Fow) {
				Collections.sort(list, new Comparator<Object>() {
					@Override
					public int compare(Object o1, Object o2) {
						// orders innings by innings of match
						return Integer.valueOf(((Fow)o1).getWicket()).compareTo(
								Integer.valueOf(((Fow)o2).getWicket()));
					}
				});
			}
			
			return list;
		}

		return super.getValue(context, base, property);
	}

}