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

# deployment to dev environment

resource "aws_api_gateway_deployment" "deployment" {
  depends_on = [
    aws_api_gateway_integration.cep,
    aws_api_gateway_integration.token
  ]

  rest_api_id       = aws_api_gateway_rest_api.dict_api.id
  stage_name        = "dev"
  stage_description = "Development environment"
}