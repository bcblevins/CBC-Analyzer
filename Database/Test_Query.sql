select test.*, parameter.parameter_id, parameter.name, result.result_value, parameter.range_low, parameter.range_high, parameter.unit
from test
join result on result.test_id = test.test_id
join parameter on parameter.parameter_id = result.parameter_id
