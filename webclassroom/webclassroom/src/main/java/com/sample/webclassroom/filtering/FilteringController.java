package com.sample.webclassroom.filtering;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@RestController
public class FilteringController {
	
	@GetMapping("/static-filter")
	public StaticBeanFilter returnStaticFilterBean() {
		return new StaticBeanFilter("value1", "value2", "value3");
	}
	
	@GetMapping("/static-filter-list")
	public List<StaticBeanFilter> returnStaticListFilterBean(){
		return Arrays.asList(new StaticBeanFilter("value1", "value2", "value3"), 
				new StaticBeanFilter("value12", "value22", "value32"));
	}
	
	@GetMapping("/dynamic-filter")
	public MappingJacksonValue returnDynamicFilter() {
		DynamicBeanFilter dy = new DynamicBeanFilter("value1", "value2", "value3");
		Set<String> props = new HashSet<String>();
		props.add("field1");
		props.add("field2");
		
		return helperMapping(dy, "dynamicFilter", props);
	}
	
	@GetMapping("/dynamic-filter-list")
	public MappingJacksonValue returnDynamicFilterList(){
		List<DynamicBeanFilter> dy = Arrays.asList(new DynamicBeanFilter("value1", "value2", "value3"), 
				new DynamicBeanFilter("value12", "value22", "value32"));
		
		Set<String> props = new HashSet<String>();
		props.add("field1");
		props.add("field3");
		
		return helperMapping(dy, "dynamicFilter", props);
	}
	
	private MappingJacksonValue helperMapping(Object obj, String propertyBeanName, Set<String> props) {
		SimpleBeanPropertyFilter filter = new SimpleBeanPropertyFilter.FilterExceptFilter(props);
		FilterProvider filters = new SimpleFilterProvider().addFilter(propertyBeanName, filter);
		
		MappingJacksonValue mapping = new MappingJacksonValue(obj);
		mapping.setFilters(filters);
		return mapping;
	}
}
