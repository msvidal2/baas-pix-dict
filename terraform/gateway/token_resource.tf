
# get token endpoint

resource "aws_api_gateway_resource" "token" {
  rest_api_id = aws_api_gateway_rest_api.dict_api.id
  parent_id   = aws_api_gateway_rest_api.dict_api.root_resource_id
  path_part   = "token"
}

resource "aws_api_gateway_method" "token" {
  rest_api_id = aws_api_gateway_rest_api.dict_api.id
  resource_id = aws_api_gateway_resource.token.id
  http_method = "POST"

  authorization = "NONE"
}

resource "aws_api_gateway_integration" "token" {
  rest_api_id = aws_api_gateway_rest_api.dict_api.id
  resource_id = aws_api_gateway_resource.token.id
  http_method = aws_api_gateway_method.token.http_method

  integration_http_method = aws_api_gateway_method.token.http_method

  type = "HTTP_PROXY"
  uri  = "https://${aws_cognito_user_pool_domain.dict_api.domain}.auth.${var.region}.amazoncognito.com/oauth2/token"
}