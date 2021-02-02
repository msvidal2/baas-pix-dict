
# Cognito user pool

resource "aws_cognito_user_pool" "dict_api" {
  name = "dict-api-users"
}

resource "aws_cognito_user_pool_domain" "dict_api" {
  domain       = "dict-api"
  user_pool_id = aws_cognito_user_pool.dict_api.id
}

resource "aws_cognito_resource_server" "dict_api" {
  identifier = "dict-api"
  name       = "dict-api"

  scope {
    scope_name        = "read"
    scope_description = "To read"
  }

  scope {
    scope_name        = "create"
    scope_description = "To create"
  }

  scope {
    scope_name        = "update"
    scope_description = "To update"
  }

  scope {
    scope_name        = "delete"
    scope_description = "To delete"
  }

  user_pool_id = aws_cognito_user_pool.pool.id
}

# clients

resource "aws_cognito_user_pool_client" "client" {
  name                          = "client-test"
  user_pool_id                  = aws_cognito_user_pool.pool.id
  supported_identity_providers  = ["COGNITO"]
  prevent_user_existence_errors = "ENABLED"

  generate_secret        = true
  refresh_token_validity = 30

  allowed_oauth_flows                  = ["client_credentials"]
  explicit_auth_flows                  = ["ALLOW_REFRESH_TOKEN_AUTH"]
  allowed_oauth_flows_user_pool_client = true
  allowed_oauth_scopes                 = [
      "${aws_cognito_user_pool_domain.dict_api.domain}/read",
      "${aws_cognito_user_pool_domain.dict_api.domain}/create",
      "${aws_cognito_user_pool_domain.dict_api.domain}/update",
      "${aws_cognito_user_pool_domain.dict_api.domain}/delete"
    ]

  depends_on = [
    aws_cognito_resource_server.dict_api
  ]
}