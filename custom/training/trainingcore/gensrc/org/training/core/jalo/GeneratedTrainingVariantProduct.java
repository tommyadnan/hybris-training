/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Nov 12, 2020, 10:19:49 AM                   ---
 * ----------------------------------------------------------------
 */
package org.training.core.jalo;

import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.variants.jalo.VariantProduct;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.training.core.constants.TrainingCoreConstants;

/**
 * Generated class for type {@link de.hybris.platform.variants.jalo.VariantProduct TrainingVariantProduct}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedTrainingVariantProduct extends VariantProduct
{
	/** Qualifier of the <code>TrainingVariantProduct.sku</code> attribute **/
	public static final String SKU = "sku";
	/** Qualifier of the <code>TrainingVariantProduct.material</code> attribute **/
	public static final String MATERIAL = "material";
	/** Qualifier of the <code>TrainingVariantProduct.activeStatus</code> attribute **/
	public static final String ACTIVESTATUS = "activeStatus";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(VariantProduct.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(SKU, AttributeMode.INITIAL);
		tmp.put(MATERIAL, AttributeMode.INITIAL);
		tmp.put(ACTIVESTATUS, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TrainingVariantProduct.activeStatus</code> attribute.
	 * @return the activeStatus - Active Status Product
	 */
	public EnumerationValue getActiveStatus(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, ACTIVESTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TrainingVariantProduct.activeStatus</code> attribute.
	 * @return the activeStatus - Active Status Product
	 */
	public EnumerationValue getActiveStatus()
	{
		return getActiveStatus( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TrainingVariantProduct.activeStatus</code> attribute. 
	 * @param value the activeStatus - Active Status Product
	 */
	public void setActiveStatus(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, ACTIVESTATUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TrainingVariantProduct.activeStatus</code> attribute. 
	 * @param value the activeStatus - Active Status Product
	 */
	public void setActiveStatus(final EnumerationValue value)
	{
		setActiveStatus( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TrainingVariantProduct.material</code> attribute.
	 * @return the material - Material Product
	 */
	public String getMaterial(final SessionContext ctx)
	{
		return (String)getProperty( ctx, MATERIAL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TrainingVariantProduct.material</code> attribute.
	 * @return the material - Material Product
	 */
	public String getMaterial()
	{
		return getMaterial( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TrainingVariantProduct.material</code> attribute. 
	 * @param value the material - Material Product
	 */
	public void setMaterial(final SessionContext ctx, final String value)
	{
		setProperty(ctx, MATERIAL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TrainingVariantProduct.material</code> attribute. 
	 * @param value the material - Material Product
	 */
	public void setMaterial(final String value)
	{
		setMaterial( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TrainingVariantProduct.sku</code> attribute.
	 * @return the sku - SKU Product
	 */
	public String getSku(final SessionContext ctx)
	{
		return (String)getProperty( ctx, SKU);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TrainingVariantProduct.sku</code> attribute.
	 * @return the sku - SKU Product
	 */
	public String getSku()
	{
		return getSku( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TrainingVariantProduct.sku</code> attribute. 
	 * @param value the sku - SKU Product
	 */
	public void setSku(final SessionContext ctx, final String value)
	{
		setProperty(ctx, SKU,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TrainingVariantProduct.sku</code> attribute. 
	 * @param value the sku - SKU Product
	 */
	public void setSku(final String value)
	{
		setSku( getSession().getSessionContext(), value );
	}
	
}
