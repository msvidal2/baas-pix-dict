terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 3.0"
    }
  }
}

provider "aws" {
  profile = var.profile
  region  = var.region
}


# Api

resource "aws_api_gateway_rest_api" "dict_api" {
  name        = "dict-api"
  description = "Dict API"

  endpoint_configuration {
    types = ["REGIONAL"]
  }
}

resource "aws_api_gateway_resource" "dict_api_v1" {
  rest_api_id = aws_api_gateway_rest_api.dict_api.id
  parent_id   = aws_api_gateway_rest_api.dict_api.root_resource_id
  path_part   = "v1"
}

# deployment to dev environment

resource "aws_api_gateway_deployment" "dev" {
  depends_on = [
    aws_api_gateway_integration.token,
    aws_api_gateway_integration.keys_create,
    aws_api_gateway_integration.keys_delete,
    aws_api_gateway_integration.keys_update,
    aws_api_gateway_integration.keys_find,
    aws_api_gateway_integration.keys_list
  ]

  rest_api_id       = aws_api_gateway_rest_api.dict_api.id
  stage_name        = "dev"
  stage_description = "Development environment"
}