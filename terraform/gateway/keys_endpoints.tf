
# keys

resource "aws_api_gateway_resource" "keys" {
  rest_api_id = aws_api_gateway_rest_api.dict_api.id
  parent_id   = aws_api_gateway_resource.dict_api_v2.id
  path_part   = "keys"
}

resource "aws_api_gateway_resource" "keys_key_path" {
  rest_api_id = aws_api_gateway_rest_api.dict_api.id
  parent_id   = aws_api_gateway_resource.keys.id
  path_part   = "{key}"
}

# create

resource "aws_api_gateway_method" "keys_create" {
  rest_api_id = aws_api_gateway_rest_api.dict_api.id
  resource_id = aws_api_gateway_resource.keys.id
  http_method = "POST"

  authorization        = "COGNITO_USER_POOLS"
  authorizer_id        = aws_api_gateway_authorizer.dict_auth.id
  authorization_scopes = ["${aws_cognito_user_pool_domain.dict_api.domain}/create"]

  request_parameters = {
    "method.request.header.requestIdentifier" = true
  }
}

resource "aws_api_gateway_integration" "keys_create" {
  rest_api_id = aws_api_gateway_rest_api.dict_api.id
  resource_id = aws_api_gateway_resource.keys.id
  http_method = aws_api_gateway_method.keys_create.http_method

  integration_http_method = aws_api_gateway_method.keys_create.http_method

  request_parameters = {
    "integration.request.header.requestIdentifier" = "method.request.header.requestIdentifier"
  }

  type = "HTTP_PROXY"
  uri  = "http://${var.dict_api_host}/v1/keys"
}

# delete

resource "aws_api_gateway_method" "keys_delete" {
  rest_api_id = aws_api_gateway_rest_api.dict_api.id
  resource_id = aws_api_gateway_resource.keys_key_path.id
  http_method = "DELETE"

  authorization        = "COGNITO_USER_POOLS"
  authorizer_id        = aws_api_gateway_authorizer.dict_auth.id
  authorization_scopes = ["${aws_cognito_user_pool_domain.dict_api.domain}/delete"]

  request_parameters = {
    "method.request.header.requestIdentifier" = true
    "method.request.path.key"                 = true
  }
}

resource "aws_api_gateway_integration" "keys_delete" {
  rest_api_id = aws_api_gateway_rest_api.dict_api.id
  resource_id = aws_api_gateway_resource.keys_key_path.id
  http_method = aws_api_gateway_method.keys_delete.http_method

  integration_http_method = aws_api_gateway_method.keys_delete.http_method

  request_parameters = {
    "integration.request.header.requestIdentifier" = "method.request.header.requestIdentifier"
    "integration.request.path.key"                 = "method.request.path.key"
  }

  type = "HTTP_PROXY"
  uri  = "http://${var.dict_api_host}/v1/keys/{key}"
}

# update

resource "aws_api_gateway_method" "keys_update" {
  rest_api_id = aws_api_gateway_rest_api.dict_api.id
  resource_id = aws_api_gateway_resource.keys_key_path.id
  http_method = "PUT"

  authorization        = "COGNITO_USER_POOLS"
  authorizer_id        = aws_api_gateway_authorizer.dict_auth.id
  authorization_scopes = ["${aws_cognito_user_pool_domain.dict_api.domain}/edit"]

  request_parameters = {
    "method.request.path.key"                 = true
    "method.request.header.requestIdentifier" = true
  }
}

resource "aws_api_gateway_integration" "keys_update" {
  rest_api_id = aws_api_gateway_rest_api.dict_api.id
  resource_id = aws_api_gateway_resource.keys_key_path.id
  http_method = aws_api_gateway_method.keys_update.http_method

  integration_http_method = aws_api_gateway_method.keys_update.http_method

  request_parameters = {
    "integration.request.path.key"                 = "method.request.path.key"
    "integration.request.header.requestIdentifier" = "method.request.header.requestIdentifier"
  }

  type = "HTTP_PROXY"
  uri  = "http://${var.dict_api_host}/v1/keys/{key}"
}

# find

resource "aws_api_gateway_method" "keys_find" {
  rest_api_id = aws_api_gateway_rest_api.dict_api.id
  resource_id = aws_api_gateway_resource.keys_key_path.id
  http_method = "GET"

  authorization        = "COGNITO_USER_POOLS"
  authorizer_id        = aws_api_gateway_authorizer.dict_auth.id
  authorization_scopes = ["${aws_cognito_user_pool_domain.dict_api.domain}/read"]

  request_parameters = {
    "method.request.path.key"                 = true
    "method.request.header.requestIdentifier" = true
    "method.request.header.userId"            = true
  }
}

resource "aws_api_gateway_integration" "keys_find" {
  rest_api_id = aws_api_gateway_rest_api.dict_api.id
  resource_id = aws_api_gateway_resource.keys_key_path.id
  http_method = aws_api_gateway_method.keys_find.http_method

  integration_http_method = aws_api_gateway_method.keys_find.http_method

  request_parameters = {
    "integration.request.path.key"                 = "method.request.path.key"
    "integration.request.header.requestIdentifier" = "method.request.header.requestIdentifier"
    "integration.request.header.userId"            = "method.request.header.userId"
  }

  type = "HTTP_PROXY"
  uri  = "http://${var.dict_api_host}/v1/keys/{key}"
}

# list

resource "aws_api_gateway_method" "keys_list" {
  rest_api_id = aws_api_gateway_rest_api.dict_api.id
  resource_id = aws_api_gateway_resource.keys.id
  http_method = "GET"

  authorization        = "COGNITO_USER_POOLS"
  authorizer_id        = aws_api_gateway_authorizer.dict_auth.id
  authorization_scopes = ["${aws_cognito_user_pool_domain.dict_api.domain}/read"]

  request_parameters = {
    "method.request.header.requestIdentifier" = true

    "method.request.querystring.cpfCnpj"       = true
    "method.request.querystring.personType"    = true
    "method.request.querystring.accountNumber" = true
    "method.request.querystring.accountType"   = true
    "method.request.querystring.branchNumber"  = false
    "method.request.querystring.ispb"          = true
  }
}

resource "aws_api_gateway_integration" "keys_list" {
  rest_api_id = aws_api_gateway_rest_api.dict_api.id
  resource_id = aws_api_gateway_resource.keys.id
  http_method = aws_api_gateway_method.keys_list.http_method

  integration_http_method = aws_api_gateway_method.keys_list.http_method

  request_parameters = {
    "integration.request.header.requestIdentifier" = "method.request.header.requestIdentifier"

    "integration.request.querystring.cpfCnpj"       = "method.request.querystring.cpfCnpj"
    "integration.request.querystring.personType"    = "method.request.querystring.personType"
    "integration.request.querystring.accountNumber" = "method.request.querystring.accountNumber"
    "integration.request.querystring.accountType"   = "method.request.querystring.accountType"
    "integration.request.querystring.branchNumber"  = "method.request.querystring.branchNumber"
    "integration.request.querystring.ispb"          = "method.request.querystring.ispb"
  }

  type = "HTTP_PROXY"
  uri  = "http://${var.dict_api_host}/v1/keys"

}


# throttling 

# resource "aws_api_gateway_method_settings" "keys_create_settings" {
#   rest_api_id = aws_api_gateway_rest_api.dict_api.id
#   stage_name = aws_api_gateway_deployment.dev.stage_name
#   method_path = "${trimprefix(aws_api_gateway_resource.keys.path, "/")}/${aws_api_gateway_method.keys_create.http_method}"

#   settings {
#     throttling_rate_limit = 10000
#     throttling_burst_limit = 5000
#   }
# }