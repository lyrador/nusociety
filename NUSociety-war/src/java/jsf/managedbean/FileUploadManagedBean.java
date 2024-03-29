/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author 65912
 */
@Named(value = "fileUploadManagedBean")
@RequestScoped
public class FileUploadManagedBean {

    private String uploadedFilePath;
    private Boolean showUploadedFile;    
    
    @Inject
    private PostManagementManagedBean postManagementSessionBean;
    
    @Inject
    private HomePagePostManagedBean homePagePostManagedBean;
    
    @Inject
    private societyPostsManagedBean societyPostsManagedBean;
    
    @Inject
    private EditProfileManagedBean editProfileManagedBean;
    
    @Inject
    private EditSocietyManagedBean editSocietyManagedBean;
        
    public FileUploadManagedBean() 
    {
        showUploadedFile = false;
    }
    
    
    //Specifically for Create Post
    public void handleFileUpload(FileUploadEvent event)
    {
        try
        {
            String newFilePath = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("alternatedocroot_1") + System.getProperty("file.separator") + event.getFile().getFileName();

            System.err.println("********** Demo03ManagedBean.handleFileUpload(): File name: " + event.getFile().getFileName());
            System.err.println("********** Demo03ManagedBean.handleFileUpload(): newFilePath: " + newFilePath);

            File file = new File(newFilePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = event.getFile().getInputStream();

            while (true)
            {
                a = inputStream.read(buffer);

                if (a < 0)
                {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();
            
            setUploadedFilePath(FacesContext.getCurrentInstance().getExternalContext().getInitParameter("uploadedFilesPath") + "/" + event.getFile().getFileName());
            System.out.println("link = " + uploadedFilePath);
            showUploadedFile = true;
            
            postManagementSessionBean.getPostToUpdate().setImage(event.getFile().getFileName());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,  "File uploaded successfully", ""));
        }
        catch(IOException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  "File upload error: " + ex.getMessage(), ""));
        }
    }

     //Specifically for Post Update
    public void handleFileUploadForUpdate(FileUploadEvent event)
    {
        try
        {
            String newFilePath = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("alternatedocroot_1") + System.getProperty("file.separator") + event.getFile().getFileName();

            System.err.println("********** Demo03ManagedBean.handleFileUpload(): File name: " + event.getFile().getFileName());
            System.err.println("********** Demo03ManagedBean.handleFileUpload(): newFilePath: " + newFilePath);

            File file = new File(newFilePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = event.getFile().getInputStream();

            while (true)
            {
                a = inputStream.read(buffer);

                if (a < 0)
                {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();
            
            setUploadedFilePath(FacesContext.getCurrentInstance().getExternalContext().getInitParameter("uploadedFilesPath") + "/" + event.getFile().getFileName());
            System.out.println("link = " + uploadedFilePath);
            showUploadedFile = true;
            
            homePagePostManagedBean.getPostToUpdate().setImage(event.getFile().getFileName());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,  "File uploaded successfully", ""));
        }
        catch(IOException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  "File upload error: " + ex.getMessage(), ""));
        }
    }
    
    public void handleFileUploadForSocietyUpdate(FileUploadEvent event)
    {
        try
        {
            String newFilePath = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("alternatedocroot_1") + System.getProperty("file.separator") + event.getFile().getFileName();

            System.err.println("********** Demo03ManagedBean.handleFileUpload(): File name: " + event.getFile().getFileName());
            System.err.println("********** Demo03ManagedBean.handleFileUpload(): newFilePath: " + newFilePath);

            File file = new File(newFilePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = event.getFile().getInputStream();

            while (true)
            {
                a = inputStream.read(buffer);

                if (a < 0)
                {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();
            
            setUploadedFilePath(FacesContext.getCurrentInstance().getExternalContext().getInitParameter("uploadedFilesPath") + "/" + event.getFile().getFileName());
            System.out.println("link = " + uploadedFilePath);
            showUploadedFile = true;
            
            societyPostsManagedBean.getPostToUpdate().setImage(event.getFile().getFileName());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,  "File uploaded successfully", ""));
        }
        catch(IOException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  "File upload error: " + ex.getMessage(), ""));
        }
    }
    
    public void handleFileUploadForCreate(FileUploadEvent event)
    {
        try
        {
            String newFilePath = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("alternatedocroot_1") + System.getProperty("file.separator") + event.getFile().getFileName();

            System.err.println("********** Demo03ManagedBean.handleFileUpload(): File name: " + event.getFile().getFileName());
            System.err.println("********** Demo03ManagedBean.handleFileUpload(): newFilePath: " + newFilePath);

            File file = new File(newFilePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = event.getFile().getInputStream();

            while (true)
            {
                a = inputStream.read(buffer);

                if (a < 0)
                {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();
            
            setUploadedFilePath(FacesContext.getCurrentInstance().getExternalContext().getInitParameter("uploadedFilesPath") + "/" + event.getFile().getFileName());
            System.out.println("link = " + uploadedFilePath);
            showUploadedFile = true;
            
            postManagementSessionBean.getNewPostEntity().setImage(event.getFile().getFileName());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,  "File uploaded successfully", ""));
        }
        catch(IOException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  "File upload error: " + ex.getMessage(), ""));
        }
    }
    
    public void handleFileUploadForEditProfile(FileUploadEvent event)
    {
        try
        {
            String newFilePath = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("alternatedocroot_1") + System.getProperty("file.separator") + event.getFile().getFileName();

            System.err.println("********** Demo03ManagedBean.handleFileUpload(): File name: " + event.getFile().getFileName());
            System.err.println("********** Demo03ManagedBean.handleFileUpload(): newFilePath: " + newFilePath);

            File file = new File(newFilePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = event.getFile().getInputStream();

            while (true)
            {
                a = inputStream.read(buffer);

                if (a < 0)
                {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();
            
            setUploadedFilePath(FacesContext.getCurrentInstance().getExternalContext().getInitParameter("uploadedFilesPath") + "/" + event.getFile().getFileName());
            System.out.println("link = " + uploadedFilePath);
            showUploadedFile = true;
            
            editProfileManagedBean.getCurrentStudent().setProfilePicture(event.getFile().getFileName());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,  "File uploaded successfully", ""));
        }
        catch(IOException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  "File upload error: " + ex.getMessage(), ""));
        }
    }
    
        public void handleFileUploadForEditSociety(FileUploadEvent event)
    {
        try
        {
            String newFilePath = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("alternatedocroot_1") + System.getProperty("file.separator") + event.getFile().getFileName();

            System.err.println("********** Demo03ManagedBean.handleFileUpload(): File name: " + event.getFile().getFileName());
            System.err.println("********** Demo03ManagedBean.handleFileUpload(): newFilePath: " + newFilePath);

            File file = new File(newFilePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = event.getFile().getInputStream();

            while (true)
            {
                a = inputStream.read(buffer);

                if (a < 0)
                {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();
            
            setUploadedFilePath(FacesContext.getCurrentInstance().getExternalContext().getInitParameter("uploadedFilesPath") + "/" + event.getFile().getFileName());
            System.out.println("link = " + uploadedFilePath);
            showUploadedFile = true;
            
            editSocietyManagedBean.getSociety().setProfilePicture(event.getFile().getFileName());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,  "File uploaded successfully", ""));
        }
        catch(IOException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  "File upload error: " + ex.getMessage(), ""));
        }
    }
    
    public String getUploadedFilePath() {
        return uploadedFilePath;
    }

    public void setUploadedFilePath(String uploadedFilePath) {
        this.uploadedFilePath = uploadedFilePath;
    }
    
    public Boolean getShowUploadedFile() {
        return showUploadedFile;
    }

    public void setShowUploadedFile(Boolean showUploadedFile) {
        this.showUploadedFile = showUploadedFile;
    }    

    public PostManagementManagedBean getPostManagementSessionBean() {
        return postManagementSessionBean;
    }

    public void setPostManagementSessionBean(PostManagementManagedBean postManagementSessionBean) {
        this.postManagementSessionBean = postManagementSessionBean;
    }

    public EditProfileManagedBean getEditProfileManagedBean() {
        return editProfileManagedBean;
    }

    public void setEditProfileManagedBean(EditProfileManagedBean editProfileManagedBean) {
        this.editProfileManagedBean = editProfileManagedBean;
    }

    public HomePagePostManagedBean getHomePagePostManagedBean() {
        return homePagePostManagedBean;
    }

    public void setHomePagePostManagedBean(HomePagePostManagedBean homePagePostManagedBean) {
        this.homePagePostManagedBean = homePagePostManagedBean;
    }

    public societyPostsManagedBean getSocietyPostsManagedBean() {
        return societyPostsManagedBean;
    }

    public void setSocietyPostsManagedBean(societyPostsManagedBean societyPostsManagedBean) {
        this.societyPostsManagedBean = societyPostsManagedBean;
    }

    public EditSocietyManagedBean getEditSocietyManagedBean() {
        return editSocietyManagedBean;
    }

    public void setEditSocietyManagedBean(EditSocietyManagedBean editSocietyManagedBean) {
        this.editSocietyManagedBean = editSocietyManagedBean;
    }
    
    
}

