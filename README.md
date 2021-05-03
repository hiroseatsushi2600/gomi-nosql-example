# README

(概要の説明)

## フレームワーク

- Spring Boot: 2.4.4

## 言語

- Kotlin: 1.4.31

## 環境

(未)

## 開発手順

- `make start-console` でコンテナを起動してbashでログイン
- `make build` でSpring + Kotlinの環境をビルド
- `make start-console` でコンテナを起動してbashでログイン
- コンソール内:
    - `gradle build` でアプリケーションのテスト&ビルド
    - `gradle bootRun` でアプリケーションの起動
    - `gradle ktlintFormat` でコードのフォーマット
- `bootRun`でアプリケーションを立ち上げた後、`http://localhost:8080/xxx/swagger-ui.html`にアクセスすることで、APIのドキュメントを確認できます。また、`http://localhost:8080/xxx/v3/api-docs.yaml`にアクセスすることでYAML形式のドキュメントを取得できます。

### CI

(未)
