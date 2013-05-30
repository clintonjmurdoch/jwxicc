package com.jwxicc.cricket.jsf.convert;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.jwxicc.cricket.entity.Review;

@FacesConverter(value = "ReviewConverter")
public class ReviewConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		// TODO Auto-generated method stub
		Review review;
		if (arg2 == null || arg2.trim() == "") {
			review = null;
		} else {
			review = new Review();
			review.setReviewText(arg2);
		}
		return review;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		// TODO Auto-generated method stub
		String toReturn;
		if (arg2 != null) {
			Review review = (Review) arg2;
			toReturn = review.getReviewText();
		} else {
			toReturn = null;
		}

		return toReturn;
	}

}
