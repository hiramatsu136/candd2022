# Manifest file for kubernetes
Google Cloud のArtifact RegistryからPodを作成するサンプル

## 利用イメージ
![scope](https://github.com/hiramatsu136/candd2022/raw/images/hiramatsu/kubernetes/springbootapi/scope.png)

### 用途
- Continuous Deliveryのトリガとして
- kubectlコマンドを使って直接デプロイ

## ファイル説明
| file | description |
| -- | -- |
|deployment.yaml | springboot application deployment image |
|service.yaml | LoadBalancer service |

### 利用例:Google CloudのGKEにコンテナ生成
gcloudが利用可能であること  
環境変数は適宜自分の設定値に置き換えること

```bash
# Create GKE mimimum claster
gcloud config set project $Myproject
gcloud container clusters create $ClusterName --zone $Zone --disk-size 10 --machine-type e2-small --num-nodes 1
gcloud container clusters get-credentials $ClusterName --zone $Zone

# deploy container on GKE
kubectl apply -f deployment.yaml
kubectl apply -f service.yaml
```
Google Cloud コンソールの[GKE]-[Services]にロードバランサが作成され、エンドポイントにアクセス可能

