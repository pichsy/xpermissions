# XFileChooser

权限请求框架，自带授权前提示弹窗功能

启动后只提示一次。

如果觉得好用给我个star吧，开源不易。
如果遇到什么问题直接提issue。

### 引入

    // 权限申请框架
    implementation 'com.github.pichsy:xpermission:1.0'


### 用法
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        XCardButton btn = findViewById(R.id.btn1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionUtils.requestPermissionsWithDefaultDialog(MainActivity.this,
                        8912, new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.READ_PHONE_STATE
                        },
                        new PermissionUtils.OnPermissionCallback() {
                            @Override
                            public void onCallback(@Nullable String... deniedPermissions) {
                                Log.d("Permissions", "deniedPermissions:" + deniedPermissions.length);
                            }
                        }
                );
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PermissionUtils.onActivityResult(this, requestCode, resultCode, data);
    }