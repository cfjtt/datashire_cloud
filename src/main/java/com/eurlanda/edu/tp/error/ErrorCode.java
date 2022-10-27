package com.eurlanda.edu.tp.error;

public enum ErrorCode {
    @ErrorMessage("操作成功")
    Success(0),

    @ErrorMessage("旧密码不正确。")
    IncorrectOldPassword(1),

    @ErrorMessage("该课程不存在")
    CourseNotExists(2),

    @ErrorMessage("该班级不存在")
    ClassNotExists(3),

    @ErrorMessage("该作业不存在")
    HomeworkNotExists(4),

    @ErrorMessage("该学生不存在")
    StudentNotExists(5),

    @ErrorMessage("该教师不存在")
    TeacherNotExists(6),

    @ErrorMessage("该学生作业不存在")
    StudentHomeworkNotExists(7),

    @ErrorMessage("该学期'%s'已存在")
    TermAlreadyExists(8),

    @ErrorMessage("该学期不存在")
    TermNotExists(9),

    @ErrorMessage("属性'头衔'不合法")
    InvalidTitle(10),

    @ErrorMessage("电子邮件'%s'格式不正确")
    InvalidEmail(11),

    @ErrorMessage("该教师工号'%s'已存在")
    TeacherAlreadyExists(12),

    @ErrorMessage("该学期'%d'不合法")
    InvalidSemester(13),

    @ErrorMessage("所选学期存在正在被班级表使用的学期")
    TermIsUsedByClass(14),

    @ErrorMessage("所选教师存在正在被课程表使用的教师")
    TeacherIsUsedByCourse(15),

    @ErrorMessage("该班级正在被作业表使用")
    ClassIsUsedByHomework(16),

    @ErrorMessage("该班级正在被学生班级表使用")
    ClassIsUsedByStudentClass(17),

    @ErrorMessage("所选课程存在正在被章节表使用的课程")
    CourseIsUsedByModule(18),

    @ErrorMessage("所选课程存在正在被班级表使用的课程")
    CourseIsUsedByClass(19),

    @ErrorMessage("该学生不在这个班级里")
    StudentNotInClass(20),

    @ErrorMessage("该章节不存在")
    ModuleNotExists(21),

    @ErrorMessage("该作业中章节与班级属于不同的课程，无法添加")
    HomeworkModuleClassBelongsToDifferentCourse(22),

    @ErrorMessage("云件类型非法")
    InvalidCloudwareType(23),

    @ErrorMessage("该作业正在被学生作业表使用")
    HomeworkUsedByStudentHomework(24),

    @ErrorMessage("该章节正在被实验表使用")
    ModuleUsedByExperiment(25),

    @ErrorMessage("该章节正在被作业表使用")
    ModuleUsedByHomework(26),

    @ErrorMessage("该学生已在该班级")
    StudentAlreadyInClass(27),

    @ErrorMessage("该学生学号'%S'已存在")
    StudentAlreadyExists(28),

    @ErrorMessage("已存在学号为'%s'的'%s'学生名字叫'%s'。是否要覆盖？")
    DuplicateStudentNoFound(29),

    @ErrorMessage("该实验不存在")
    ExperimentNotFound(30),

    @ErrorMessage("该实验正在被学生实验表使用")
    ExperimentUsedByStudentExperiment(31),

    @ErrorMessage("该用户已存在")
    UserAlreadyExists(32),

    @ErrorMessage("该实体已存在")
    EntityAlreadyExists(33),

    @ErrorMessage("该学生实验已存在")
    StudentExperimentExists(34),

    @ErrorMessage("该学生实验不存在")
    StudentExperimentNotFound(35),

    @ErrorMessage("该学生作业已存在")
    StudentHomeworkExists(36),

    @ErrorMessage("用户名或密码不正确")
    BadCredential(37),

    @ErrorMessage("该文件类型错误")
    FileTypeError(38),

    @ErrorMessage("该文件不为图片")
    FileIsNotImage(39),

    @ErrorMessage("该文件不为Markdown")
    FileIsNotMarkdown(40),

    @ErrorMessage("该文件不为Word或PDF")
    FileIsNotReport(41),

    @ErrorMessage("该文件不为deep")
    FileIsNotDEEP(71),

    @ErrorMessage("文件不存在")
    FileIsNotExist(42),

    @ErrorMessage("该用户不存在")
    UserNotExist(43),

    @ErrorMessage("该章节资源不存在")
    ModuleResourceNotFound(44),

    @ErrorMessage("会话已过期，请重新登录")
    TokenExpired(45),

    @ErrorMessage("请登录")
    NeedAuthentication(46),

    @ErrorMessage("云件不存在")
    CloudwareNotExist(47),

    @ErrorMessage("该老师未开班级")
    TeacherIsNotInClass(48),

    @ErrorMessage("该老师没有布置作业")
    TeacherHasNotHomework(49),

    @ErrorMessage("报告文件只支持pdf格式")
    InvalidReportType(50),

    @ErrorMessage("Excel文件内容格式不正确")
    InvalidExcelFileFormat(51),

    @ErrorMessage("两次新密码输入不一致")
    NewPasswordsNotTheSame(52),

    @ErrorMessage("一个用户仅能同时打开%d个实验容器")
    CannotCreateExCloudwareMultipleTime(53),

    @ErrorMessage("该教师正在被班级表使用")
    TeacherIsUsedByClass(54),

    @ErrorMessage("性别只能为男或者女")
    InvalidGender(55),

    @ErrorMessage("教师工号不合法")
    TEACHERNovalidNo(56),

    @ErrorMessage("教师职称只能为教授,副教授,讲师")
    InvalidTeacherTitle(57),

    @ErrorMessage("秘钥不正确")
    InvalidSecret(58),

    @ErrorMessage("该课程已经存在")
    CourseAlreadyExisted(59),

    @ErrorMessage("该班级已存在")
    ClassAlreadyExisted(60),

    @ErrorMessage("文件已存在")
    FileIsExist(61),

    @ErrorMessage("姓名不能为空")
    NameIsNULL(62),

    @ErrorMessage("联系方式不正确")
    CONCAT_IS_VALID(63),
    @ErrorMessage("该教师工号已被学生用户使用")
    USERISNOTTEACHER(64),
    @ErrorMessage("该学生学号已被老师用户使用")
    USERISNOTSTUDENT(65),

    @ErrorMessage("该实验已存在")
    ExperimentAlreadyExisted(66),

    @ErrorMessage("该章节已存在")
    ModuleIsExists(67),
    @ErrorMessage("教师工号不能为空")
    TEACHERNoTNULLNo(68),
    @ErrorMessage("年级不能为空")
    NoTNULLGRADE(69),
    @ErrorMessage("年级字符长度不能超过'%s'个字符")
    GRADELENGTH(70),
    @ErrorMessage("姓名不能超过'%s'个字符")
    NameLENGTH(72),
    @ErrorMessage("教师工号不能超过'%s'个字符")
    TEACHERNOLENGTH(73),
    @ErrorMessage("学生学号不能超过'%s'个字符")
    STUDENTNOLENGTH(74),
    @ErrorMessage("学生学号不能为空")
    STUDENTNoTNULLNo(75),
    @ErrorMessage("学生学号不合法")
    STUDENTInvalid(76),

    @ErrorMessage("导入的课程文件不合法")
    CoursePackageInvalid(77),
    @ErrorMessage("该学生正在被班级表使用")
    StudentIsUsedByClass(78),
    @ErrorMessage("联系方式不能超过'%s'个字符")
    PhoneLength(79),
    @ErrorMessage("导出课程失败！")
    ExportError(80),
    @ErrorMessage("导入课程失败！")
    ImportError(81),
    @ErrorMessage("导入squidFlow失败")
    ImportSquidFlowError(82),

    @ErrorMessage("违法的参数")
    InvalidParam(85),

    @ErrorMessage("违法的参数，请联系管理员！")
    ImportTrainingDbOrCloudDbError(83),


    @ErrorMessage("保存实验错误，请检查上传的markdown文件编码格式,要求为utf-8")
    EncodingError(84),

    @ErrorMessage("系统未激活，请联系管理员，谢谢！")
    SystemIsNotActivated(86),

    @ErrorMessage("该系统已过期，请联系管理员，谢谢！")
    SystemAlreadyExpired(87),

    @ErrorMessage("已达到系统允许最大在线用户数，请联系管理员，谢谢！")
    ExceedTheMaximumNumberOfOnline(88),

    @ErrorMessage("该账号已在其他地方登录，是否将其他地方的登录下线？")
    RepeatLogin(89),

    @ErrorMessage("该账号已在其他地方登录！")
    Offline(90),

    @ErrorMessage("重复登录但不踢出已经登录的用户")
    NoOffline(92),

    @ErrorMessage("重置token")
    RefreshToken(93),

    @ErrorMessage("获取key出错，请联系管理员")
    GetSysKeyError(94),

    @ErrorMessage("未导入license")
    NoImportLicense(95),

    @ErrorMessage("导入license出错，请联系管理员")
    ImportLicenseError(96),

    @ErrorMessage("系统授权码错误，请联系管理员！")
    LicenseIsUpdated(97),

    @ErrorMessage("您导入的授权码有误，请核对！")
    InvalidLicense(98),

    @ErrorMessage("您输入的授权码与系统当前授权码相同，请核对！")
    RepeatLicense(99),
    @ErrorMessage("表不存在!")
    DBTABLENOTEXISTS(100),
    @ErrorMessage("上传失败!")
    UPLOADFILE(101),
    @ErrorMessage("该作业已取消!")
    ANSWERNOTEXISTS(102),

    @ErrorMessage("您的账号已被管理员重置密码，请重新登录！")
    AlreadyResetPassword(103),

    @ErrorMessage("科研项目名称不能为空！")
    RESEARCHNAMENULL(104),

    @ErrorMessage("您没有权限操作！")
    WITHOUTAUTHORITY(105),

    @ErrorMessage("科研项目已存在！")
    RESEARCHNAMEISEXISTS(106),

    @ErrorMessage("项目对应的套餐不存在")
    RESEARCHPACKAGENOTEXISTS(107),

    @ErrorMessage("超过允许创建的项目数")
    OVERLIMITRESEARCHPROJECTNUM(108),

    @ErrorMessage("文件夹已存在")
    FolerIsExist(109),
    @ErrorMessage("文件夹不存在")
    FolerIsNotExist(110),
    @ErrorMessage("该文件夹下包含文档,请先删除文档!")
    FolerExistDoc(111),
    @ErrorMessage("该文档已存在!")
    DocIsExist(112),
    @ErrorMessage("该文档不存在!")
    DocIsNotExist(113),
    @ErrorMessage("保存失败,该文档已被其他成员编辑过!")
    DocSaveError(114),
    @ErrorMessage("该项目不存在!")
    PROJECTISNOTEXISTS(115),
    @ErrorMessage("该用户存在作为组长的科研项目")
    USERISPROJECTMANAGER(116),
    @ErrorMessage("该用户与课程和项目存在关联，没法删除")
    USERASSOCATWITHCOURSEANDPROJECT(117),
    @ErrorMessage("服务器出错，请联系管理员")
    GeneralError(Integer.MAX_VALUE);

    private int code;

    ErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getErrorStringFormat(){
        String format = "";
        try{
            format = ErrorCode.class.getDeclaredField(this.toString()).getAnnotation(ErrorMessage.class).value();
        }catch (NoSuchFieldException ex){
            //Impossible to occur
        }
        return format;
    }
}
