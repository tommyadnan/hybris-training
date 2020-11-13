/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Nov 12, 2020, 10:19:49 AM                   ---
 * ----------------------------------------------------------------
 *  
 * [y] hybris Platform
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.commerceservices.jalo.process;

import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.processengine.jalo.BusinessProcess;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.training.core.constants.TrainingCoreConstants;

/**
 * Generated class for type {@link de.hybris.platform.commerceservices.jalo.process.TestActionProcess TestActionProcess}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedTestActionProcess extends BusinessProcess
{
	/** Qualifier of the <code>TestActionProcess.something</code> attribute **/
	public static final String SOMETHING = "something";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(BusinessProcess.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(SOMETHING, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestActionProcess.something</code> attribute.
	 * @return the something - something
	 */
	public String getSomething(final SessionContext ctx)
	{
		return (String)getProperty( ctx, SOMETHING);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestActionProcess.something</code> attribute.
	 * @return the something - something
	 */
	public String getSomething()
	{
		return getSomething( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestActionProcess.something</code> attribute. 
	 * @param value the something - something
	 */
	public void setSomething(final SessionContext ctx, final String value)
	{
		setProperty(ctx, SOMETHING,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestActionProcess.something</code> attribute. 
	 * @param value the something - something
	 */
	public void setSomething(final String value)
	{
		setSomething( getSession().getSessionContext(), value );
	}
	
}
