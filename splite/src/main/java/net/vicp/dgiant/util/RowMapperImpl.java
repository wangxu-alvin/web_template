package net.vicp.dgiant.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import net.vicp.dgiant.exception.RawResultPaginationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.support.DatabaseResults;

public class RowMapperImpl<T> implements RowMapper<T> {
	
	private Logger logger = (Logger) LoggerFactory
			.getLogger(RowMapperImpl.class);
	
	private Class<T> clazz;
	
	public RowMapperImpl(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public T mapRow(DatabaseResults rs)
			throws RawResultPaginationException {
		
		//FIXME should use columns for iteration
		T entry;
		try {
			entry = clazz.newInstance();
			List<String> columns = Arrays.asList(rs.getColumnNames());
			
			Field[] fields = clazz.getDeclaredFields();
			
			for (Field field : fields) {
				
				DatabaseField dbFiled = field.getAnnotation(DatabaseField.class);
				String columnName = dbFiled.columnName() != null ? dbFiled.columnName() : field.getName();
				
				Method setMethod = clazz.getMethod(setter(field), field.getType());
				
				if (field.getType() == Integer.class) {
					
					setMethod.invoke(entry, rs.getInt(columns.indexOf(columnName)));
					
				} else if (field.getType() == String.class) {
					
					setMethod.invoke(entry, rs.getString(columns.indexOf(columnName)));
					
				} else if (field.getType() == Date.class) {
					
					if (DataType.DATE_STRING == dbFiled.dataType()) {

						setMethod
								.invoke(entry,
										new SimpleDateFormat(
												Constants.DEFAULT_DATE_FORMAT).parse(rs.getString(columns
												.indexOf(columnName))));

					} else if (DataType.DATE_LONG == dbFiled.dataType()) {
						
						setMethod.invoke(
								entry,
								new Date(1000 * rs.getLong(columns
										.indexOf(columnName))));
						
					}
					
				} else {
					
					logger.error(field.getType().getName()
							+ "is not supported by RawResultPagination object");
					
				}
			}
		} catch (Exception e) {
			
			logger.error(e.getMessage());
			
			throw new RawResultPaginationException(e.getMessage());
		}
		
		return entry;
	}
	
	/**
	 * setter method name of the field
	 * @param field
	 * @return
	 */
	private String setter (Field field) {
		
		String name = field.getName();

		if (name.length() == 1) {
			return "set" + name.toUpperCase();
		}
		
		return "set" + name.toUpperCase().charAt(0) + name.substring(1);
	}

}
