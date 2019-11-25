# gitlab-project-member-exporter

GitLabの各プロジェクトのメンバー一覧を出力するツールです。

## 利用方法

実行にはJava(JDK8以上)が必要となります。

下記から最新の実行ファイル(`gitlab-project-member-exporter-x.x.x-all.jar`)を入手します。

* https://github.com/onozaty/gitlab-project-member-exporter/releases/latest

入手したjarファイルを指定してアプリケーションを実行します。

```
java -jar gitlab-project-member-exporter-1.0.0-all.jar -c config.json -pm project-members.csv -p projects.csv -u users.csv
```

GitLabのAPIを利用して、下記の情報がCSVファイルとしてエクスポートされます。

* プロジェクトメンバ一覧
* プロジェクト一覧
* ユーザ一覧

## コマンドの説明

```console
usage: java -jar gitlab-project-member-exporter-all.jar -c <config file> [-pm <project members file>] [-p <projects file>] [-u <users file>]
 -c <config file>             Config file path.
 -pm <project members file>   Project members export file path.
 -p <projects file>           Projects export file path.
 -u <users file>              Users export file path.
```

引数として下記を指定できます。

### `-c <config file>`

設定ファイルのパスです。必須項目となります。

設定ファイルにはGitLabのURLと、パーソナルアクセストークンを記載します。

```json
{
  "gitLabUrl": "https://gitlab.example.com",
  "personalAccessToken": "Dw7T7R37NEQRgYiMp4uT"
}
```

パーソナルアクセストークンの取得方法は、下記をご参照ください。

* [Personal access tokens \| GitLab](https://docs.gitlab.com/ee/user/profile/personal_access_tokens.html)

### `-pm <project members file>`

プロジェクトメンバー一覧の出力先ファイルパスです。  
CSV形式で下記の情報が出力されます。

* `Project: Name`
* `Project: Visibility`
* `User: Name`
* `User: Username`
* `User: State`
* `Permission`

```csv
Project: Name,Project: Visibility,User: Name,User: Username,User: State,Permission
Administrator / my-project,private,Administrator,root,active,Maintainer
group1 / project-b,private,Administrator,root,active,Owner
group1 / project-b,private,user2,user2,active,Developer
group1 / project-a,private,Administrator,root,active,Owner
group1 / project-a,private,user2,user2,active,Developer
group1 / project-a,private,user1,user1,blocked,Maintainer
```

グループに対して権限が付与されていた場合、グループ配下の各プロジェクトに権限があるものとして出力します。

### `-p <projects file>`

プロジェクト一覧の出力先ファイルパスです。  
CSV形式で下記の情報が出力されます。

* `Name`
* `Visibility`
* `Created at`
* `Last activity at`

```csv
Name,Visibility,Created at,Last activity at
Administrator / my-project,private,2019-11-16T13:48:47.727Z,2019-11-16T13:48:47.727Z
group1 / project-b,private,2019-11-16T13:48:11.161Z,2019-11-24T15:08:11.024Z
group1 / project-a,private,2019-11-16T13:44:20.849Z,2019-11-16T13:44:20.849Z
```

### `-u <users file>`

ユーザ一覧の出力先ファイルパスです。  
CSV形式で下記の情報が出力されます。

* `Name`
* `Username`
* `Email`
* `State`
* `Admin`
* `Created at`
* `Last sign in at`
* `Last activity on`

```csv
Name,Username,Email,State,Admin,Created at,Last sign in at,Last activity on
user3,user3,user3@example.com,active,false,2019-11-16T13:37:15.885Z,,
user2,user2,user2@example.com,active,false,2019-11-16T13:36:29.651Z,,
user1,user1,user1@example.com,blocked,false,2019-11-16T13:35:44.406Z,,
Administrator,root,admin@example.com,active,true,2019-11-16T07:50:47.821Z,2019-11-24T14:50:54.996Z,2019-11-25
```

## ビルド方法

ソースコードからビルドして利用する場合、Java(JDK8以上)がインストールされた環境で、下記コマンドでアプリケーションをビルドします。

```
gradlew shadowJar
```

`build/libs/gitlab-project-member-exporter-x.x.x-all.jar`という実行ファイルが出来上がります。(`x.x.x`はバージョン番号)
