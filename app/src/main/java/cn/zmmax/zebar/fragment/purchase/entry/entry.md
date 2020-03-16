**查询请求URL：/app/entry/queryEntry**

**请求参数**
```json
{
  "dnNo": "string",
  "materialCode": "string",
  "dnItem": 0
}
``` 
**返回参数**

```json
{
  "code": 0,
  "data": [
    {
      "amount": 0,
      "batchNo": "string",
      "locationCode": "string",
      "materialCode": "string",
      "materialName": "string",
      "spec": "string",
      "unit": "string"
    }
  ],
  "msg": "string",
  "success": true
}
```

**提交请求URL：/app/entry/submitEntry**

**请求参数**

```json
[{
  "amount": 0,
  "batchNo": "string",
  "locationCode": "string",
  "materialCode": "string",
  "materialName": "string",
  "spec": "string",
  "unit": "string"
}]
```

**返回参数**

```json
{
  "code": 0,
  "data": true,
  "msg": "string",
  "success": true
}
```